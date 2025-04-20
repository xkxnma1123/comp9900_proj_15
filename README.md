Welcome to our project backend code base.  
We are the group Danish-F10A.  
Our project is a social platform that integrates coin functions.  
This git repo has already been deployed; you can go through the front end easily and ignore the backend code.  
If you want to run this backend code locally,   
you should go to the location /comp9900_proj_15 first, and follow these instructions:  
mvn clean install  
mvn spring-boot:run  

Some prerequisites：

Java 1.8.x+  
Maven 3.6+  
MySQL 5.7+  

application.properties
And make sure to modify the database configuration to your own:  
        spring.datasource.url=    
        spring.datasource.username=    
        spring.datasource.password=    
        
The project uses Gmail SMTP service to send verification code emails.   
If you want to use your own email, please modify:  
        spring.mail.host=smtp.gmail.com  
        spring.mail.port=587  
        spring.mail.username=your email address  
        spring.mail.password=16 digits app password  
          


If you want to check our backend codebase deployment situation, please contact us via email z5530772@ad.unsw.edu.au, and I will provide you with the .pem file.
