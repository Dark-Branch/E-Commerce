### products - /products/

  - query for category
    - /products/category/{category}
    - query params 
      - subCategory
      - page
      - limit - how many results needed
  - product specific details
    - get /products/{id}
  - add product
    - post /products/ 
    - body - product
    - no object id
  - update product
    - put /products/{id}
    - body - new product

### User /users
- add user
  - post /users/
  - body - user
- get user
  - get /users/{id}

### Cart /cart
- create cart
  - post /cart
  - body - cart
  - returns new cart uri
- get cart
  - get /cart/{id}
- update cart
  - patch /cart/{id}/addItem
  - body - cartItem

### Orders /orders
- get orders of user
  - get /orders/{userid}
- create
  - post /orders
  - body - order