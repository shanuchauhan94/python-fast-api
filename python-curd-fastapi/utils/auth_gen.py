
from datetime import datetime, timedelta, timezone
from jose import JWTError, jwt

from passlib.context import CryptContext
from fastapi.security import OAuth2PasswordBearer
from fastapi import Depends, HTTPException, status
from sqlalchemy.orm import Session
from entity.models import User
from utils.db_config import init_db

SECRET_KEY = "my-super-secret-key-change-this"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_SECONDS = 1

pwd_context = CryptContext(
    schemes=["bcrypt"],
   # schemes=["Argon2"],
    deprecated="auto"
)

auth2_schema = OAuth2PasswordBearer(
     tokenUrl="/login"
)


def hash_password(password: str):
    return pwd_context.hash(password)


def verify_password(plain_password: str, hash_password: str):
    return pwd_context.verify(
        plain_password,
        hash_password
    )


def create_access_token(data: dict):
    to_encode = data.copy()

    expire = datetime.now(timezone.utc) + timedelta(
        seconds=ACCESS_TOKEN_EXPIRE_SECONDS
    )

    to_encode.update(
        {
            "exp":expire
        }
    )

    token = jwt.encode(
        to_encode,
        SECRET_KEY,
        algorithm=ALGORITHM
    )
    return token


def get_current_user(token: str = Depends(auth2_schema), db: Session=Depends(init_db)):

    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Invalid or expired token",
        headers={
            "WWW-Authenticate": "Bearer"
        }
    )
    try:
        payload = jwt.decode(
            token,
            SECRET_KEY,
            algorithms=[ALGORITHM]
        )
        username = payload.get("sub")

        if username is None:
            raise credentials_exception

    except JWTError:
        raise credentials_exception

    user = db.query(User).filter(User.username == username).first()

    if user is None:
        raise credentials_exception

    return user
