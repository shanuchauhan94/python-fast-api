
from sqlalchemy import engine, create_engine
from sqlalchemy.orm import declarative_base, sessionmaker


# PostgreSQL connection information
DATABASE_URL = "postgresql+psycopg2://shanu:root@localhost:5432/python_db"

 # Create SQLAlchemy engine
engine = create_engine(DATABASE_URL)

   # Create a session factory

session_factory = sessionmaker(
    autoflush=False,
    bind=engine
)

 # Base class for SQLAlchemy ORM models
Base = declarative_base()


# FastAPI database dependency injection , db session per request
def init_db():
    db = session_factory() # Create a new database session

    try:
        yield db  # Give the session to the FastAPI endpoint, convert function into generator and emit session lazily and not terminate fn 

    finally:
        db.close()  # Always close the session after the request    


