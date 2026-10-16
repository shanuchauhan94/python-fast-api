from fastapi import APIRouter, HTTPException, status, Depends
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from sqlalchemy import and_

from entity.schema import UserCreate, UserResponse, Token
from entity.models import User
from utils.db_config import init_db
from utils.auth_gen import hash_password, verify_password, create_access_token

router = APIRouter(
    prefix="/v1",
    tags=["user","auth"]
)


@router.post("/register", response_model=UserResponse)
def register(user: UserCreate, db: Session=Depends(init_db)):
    existing_user = db.query(User).filter(and_(User.email == user.email, User.username == user.username)).first()

    if existing_user:
        raise HTTPException(
            status_code=400,
            detail="User with this name and email already exists"
        )

    hashed_password = hash_password(user.password)

    new_user = User(
        username=user.username,
        email=user.email,
        password=hashed_password
    )

    try:
        db.add(new_user)
        db.commit()
        db.refresh(new_user)
    except Exception:
        db.rollback()
        print("save operation failed")  

    return new_user
          

@router.post("/login", response_model=Token)
def login(req_user: UserCreate, db: Session= Depends(init_db)):

    user = db.query(User).filter(User.username == req_user.username).first()
    if not user:
        raise HTTPException(
            status_code=401,
            detail="Invalid username or password"
        )
    verify_pass = verify_password(
        req_user.password,
        user.password
    )

    if not verify_pass:
        raise HTTPException(
            status_code=401,
            detail="Invalid username or password"
        )

    access_token = create_access_token(
        data={
            "sub":user.username
        }
    )

    response = {
        "access_token": access_token,
        "token_type": "bearer"
    }

    return response


    


