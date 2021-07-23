# WarehouseManagement

This is a Spring Boot REST application for **Warehouse Management**.

### **Prerequisites**
- JDK 11+
- Maven


### **Build**
This project is maven project so can be build using following maven command

    mvn clean install

### **Deploy**
- Run com.pranitpatil.WarehouseManagementApp from your IDE
OR
- Run following command after maven build

        java -jar WarehouseManagement-1.0-SNAPSHOT.jar

### **API Information**
API can be accessed from following swagger url
        http://localhost:8090/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/

Following endpoints can be used

####    Product Endpoints
- ####  Get All Available Products
  - Gets all products with their available quantity. 
  - This is a API with pagination and sorting,
  - Provide page details as query parameters.
  
      Sample Request
    
           curl --location --request GET http://localhost:8090/rest/product/availableProducts?page=0&size=10 
    
      Sample Response (With HTTP Status Code - 200)
    
           {
               "entity": [
                   {
                       "id": 1,
                       "name": "Dining Chair",
                       "price": 0.0,
                       "availableQuantity": 1,
                       "links": [
                           {
                               "rel": "product",
                               "href": "http://localhost:8090/rest/product/1"
                           }
                       ]
                   },
                   {
                       "id": 2,
                       "name": "Dinning Table",
                       "price": 0.0,
                       "availableQuantity": 1,
                       "links": [
                           {
                               "rel": "product",
                               "href": "http://localhost:8090/rest/product/2"
                           }
                       ]
                   },
                   {
                       "id": 3,
                       "name": "New 2",
                       "price": 10.0,
                       "availableQuantity": 0,
                       "links": [
                           {
                               "rel": "product",
                               "href": "http://localhost:8090/rest/product/3"
                           }
                       ]
                   }
               ],
               "pageNumber": 0,
               "totalPages": 1,
               "totalItems": 3
           }


- ####  Sell Product
  Registers a sell of product and quantity and reduces the article stock accordingly.
  
    Sample Request
  
          curl --location --request POST http://localhost:8090/rest/product/sell/{productId}?quantity={sellQuantity}
  
    Successful response is with HTTP status code 200s 


- ####  Get Product
  Gets the details of the product
  
  Sample Request

        curl --location --request GET 'http://localhost:8090/rest/product/{productId}'

  Sample Response

        {
            "id": 1,
            "name": "Dining Chair",
            "price": 0.0,
            "contain_articles": [
                {
                    "links": [
                        {
                            "rel": "article",
                            "href": "http://localhost:8090/rest/article/1"
                        }
                    ],
                    "art_id": "1",
                    "amount_of": 4
                }
            ]
        }
        

- #### Add new Product
  Creates a new product and returns it.
  
    Sample Request
  
          curl --header "Content-Type: application/json" \
            --request POST \
            --data '{"name":"New 2","price":10.0,"contain_articles":[{"art_id":"10","amount_of":4}]}' \
            http://localhost:8090/rest/product
  
    Sample Response (With HTTP Status Code - 201)
  
          {
              "id": 3,
              "name": "New 2",
              "price": 10.0,
              "contain_articles": [
                  {
                      "links": [
                          {
                              "rel": "article",
                              "href": "http://localhost:8090/rest/article/10"
                          }
                      ],
                      "art_id": "10",
                      "amount_of": 4
                  }
              ]
          }

####    Article Endpoints
- ####  Get Article
  Gets the details of the article
  
  Sample Request

        curl --location --request GET 'http://localhost:8090/rest/article/(articleId)'

  Sample Response

        {
            "name": "leg",
            "stock": 12,
            "art_id": "1"
        }
        
        
- #### Add new article
  Creates a new article and returns it.
  
    Sample Request
  
          curl --header "Content-Type: application/json" \
            --request POST \
            --data '{"name": "test","stock": 19,"art_id": "20"}' \
            http://localhost:8090/rest/article
  
    Sample Response (With HTTP Status Code - 201)
  
          {
              "name": "test",
              "stock": 19,
              "art_id": "20"
          }
            

- ####  Update Article
  Updates and existing article
     
     Sample Request
       
               curl --header "Content-Type: application/json" \
                 --request PUT \
                 --data '{"name": "test","stock": 19,"art_id": "20"}' \
                 http://localhost:8090/rest/article
       
     Sample Response (With HTTP Status Code - 200)
   
           {
               "name": "test",
               "stock": 19,
               "art_id": "20"
           }
                 

- ####  Delete Article
  Deletes the article
  
  Sample Request

        curl --location --request DELETE 'http://localhost:8090/rest/article/{articleId}'

  Successful response is empty with Http Status Code 204 No Content

   

### **Future Improvements**

- Data can be saved/loaded in mongodb instead of relational database.
    - Data model suitable for document based data store
    - Better performance
    - Faster data load through JSON files
-


### **Author**
##### **Pranit Patil**