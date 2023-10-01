# clearSolutions
RESTful API USERS

How to start: 

 1.Clone project from repository
  
2.Create database
  
   2.1 Change credential in the configuration file(url,user,password)  
  
   2.1 Or use docker enter command in terminal:
  
    - docker compose up.

 Api requests:

 Post /api/v1/users - creat user

 Put /api/v1/users/{id} - update user

 Patch /api/v1/users/{id} update user email

 Get /api/v1/users get users by dates 

Jsons :
-------------------------------------
 {

 "email": "usere1231231@xamplem.com",

 firstName": "Serhii",

 "lastName": "shapoval",

 "birthDate": "1991-01-01",

 "address": {

 "country":"Ukraine",

 "apartment":"123",

 "street": "123 Main St",

 "streetNumber":"1234",

 "city": "Example City",

 "zip": "12345"

 },

 "phoneList": [

 "123-456-7890",

 "987-654-3210"

]

}

-------------------------------------
{

"email":"updateUser@email.com"

}

-------------------------------------    

{

"fromDate":"1900-01-01",

"toDate":"1995-01-01"

}
