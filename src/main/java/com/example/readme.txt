MAINMETHOD
this is the main method where the spring boot will run

beanMappingORdao --> Folder
this is the POJO/DAO/mapping class which represents the database's table and the columns in it.
Also  called as entity class

repository --> Folder
Repository that has the interface(s) which provide methods for fetching the values from the database without explicitly using sql queries.
Can also create custom interface(s) and methods in it to fetch the values which are not offered by the jparepository interface.


resource --> Folder
Controller class that has the implementation of the http methods(get, post, put..) mapping.
This class serves as the resource mapping/controller

businessLogicORservice --> Folder
Business logic/service class that used to define the logics as methods and call them in controller classes instead of writing them directly in controller class.
