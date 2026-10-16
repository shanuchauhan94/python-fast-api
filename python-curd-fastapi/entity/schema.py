from pydantic import BaseModel, Field


class UserCreate(BaseModel):
    username: str
    email: str
    password: str = Field(min_length=8, max_length=72)


class UserResponse(BaseModel):
    id: int
    username: str
    email: str

    class Config:
        from_attributes = True


class Token(BaseModel):
    access_token: str
    token_type: str


class ProductCreate(BaseModel):
    name: str
    description: str
    price: float
    quantity: int
    manufacturer: str


class ProductResponse(BaseModel):
    id: int
    name: str
    description: str
    price: float
    quantity: int
    manufacturer: str
    owner_id: int

    class Config:
        from_attributes = True

