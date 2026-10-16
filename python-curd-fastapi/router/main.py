from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from utils.db_config import Base, engine

from router.auth_router import router as auth_router
from router.product_router import router as product_router

# Create database tables
Base.metadata.create_all(bind=engine)


app = FastAPI(
    title="FastAPI JWT CRUD API"
)

# CORS configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",
        "http://127.0.0.1:5173"
    ],
    allow_credentials=True,
    allow_methods=["GET","POST","PUT","DELETE","OPTIONS"],
    allow_headers=["*"],
)

# app.include_router([auth_router,product_router])
app.include_router(auth_router)
app.include_router(product_router)


@app.get("/", description="welcome note")
def welcome():
    return "Hello from main FastAPI => APIRouter[FastAPI]"

