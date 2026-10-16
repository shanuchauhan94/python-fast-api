from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from utils.db_config import init_db
from entity.models import Product, User
from entity.schema import ProductCreate, ProductResponse

from utils.auth_gen import get_current_user


router = APIRouter(
    prefix="/products",
    tags=["Products"]
)

# CREATE
@router.post("/", response_model=ProductResponse)
def create_product(product: ProductCreate, db: Session = Depends(init_db), current_user: User = Depends(get_current_user)):

    new_product = Product(
        name=product.name,
        description=product.description,
        price=product.price,
        quantity=product.quantity,
        owner_id=current_user.id
    )

    db.add(new_product)
    db.commit()
    db.refresh(new_product)

    return new_product


# READ ALL
@router.get("/", response_model=list[ProductResponse])
def get_products(db: Session = Depends(init_db), current_user: User = Depends(get_current_user)):

    products = db.query(Product).filter(
        Product.owner_id == current_user.id
    ).all()

    return products


# READ ONE
@router.get("/{product_id}", response_model=ProductResponse)
def get_product(product_id: int, db: Session = Depends(init_db), current_user: User = Depends(get_current_user)):

    product = db.query(Product).filter(
        Product.id == product_id,
        Product.owner_id == current_user.id
    ).first()

    if not product:
        raise HTTPException(
            status_code=404,
            detail="Product not found"
        )

    return product

# UPDATE
@router.put("/{product_id}",response_model=ProductResponse)
def update_product(product_id: int, product_data: ProductCreate, db: Session = Depends(init_db), current_user: User = Depends(get_current_user)):

    product = db.query(Product).filter(
        Product.id == product_id,
        Product.owner_id == current_user.id
    ).first()

    if not product:
        raise HTTPException(
            status_code=404,
            detail="Product not found"
        )

    product.name = product_data.name
    product.description = product_data.description
    product.price = product_data.price
    product.quantity = product_data.quantity

    db.commit()
    db.refresh(product)

    return product


# DELETE
@router.delete("/{product_id}")
def delete_product(product_id: int, db: Session = Depends(init_db), current_user: User = Depends(get_current_user)):

    product = db.query(Product).filter(
        Product.id == product_id,
        Product.owner_id == current_user.id
    ).first()

    if not product:
        raise HTTPException(
            status_code=404,
            detail="Product not found"
        )

    db.delete(product)
    db.commit()

    return {
        "message": "Product deleted successfully"
    }

