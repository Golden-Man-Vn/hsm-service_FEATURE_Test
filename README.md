1. How to build
   * Prerequirement:
	   - IDE usage: IntelliJ IDEA 2021.1.3 or higher
	   - Java for project: Java 17 or higher
		 Java language level Java 16 (compiled class)
	   - Maven: Bundled Maven 3.6.3 or higher  
	   - Internet connected so it can connect to online DB such as supabase or neondb Postgres DB provider (already integrated in this project source)
   * Build:		
		1. Open project by IntelliJ by refer to hsm-service_FEATURE_Test directory => wait for load complete => use Maven to package or build

2. How to run
	Prerequirement after build this project successfully!
	
   * Windows:
		- Install Java 17 or higher: such as Amazon Corretto jdk17.0.15_6	
          To be sure Java is configured in system path (so can use RUN\start.bat directly below)
		  If not, please place java to specific directory in RUN\start.bat (by editing line: java -jar hsm-service-1.0.jar)
		- About hsm-service_FEATURE_Test\RUN\start.bat file
		  It is a script to copy application file from build directory (target\hsm-service-1.0.jar)
		  And configure some evironment variables (DB, Flyway, .. => so if want to change another DB, please correct these variables)
		  Then run application automattically by double click on this file
		- Run:
			Double click (or run in command line) on hsm-service_FEATURE_Test\RUN\start.bat
			
	* Linux familiar:
		- Install Java 17 or higher
		- Copy application file hsm-service-1.0.jar to a specific directory
		- Run application directly in this directory by command line:
			java -jar hsm-service-1.0.jar
			
3. How to test the application
   Two ways to test the application:
   - Open Postman.json file in POSTMAN and correct URL (IP) so that it can connect to host that deploy the application
     Refer to 8seneca_Task assignment_ANSWER.xlsx file or online file (https://docs.google.com/spreadsheets/d/1qghY4qz9eqOIuHN_iU7i4cyM5-w40dJANRmGZhH0XjU/edit?gid=0#gid=0)
	 to understand each API function (input and output)
   - Run JUnit test from build IDE by refer to: 
	 hsm-service_FEATURE_Test\src\main\java\com\company\project\test
	 Scanario for JUnit test as following:
		- (1) Run testcase in UsersServiceTest: to create users
		- (2) Run testcase in TaskDetailServiceTest: to create tasks detail
		- (3) Run testcase in TasksServiceTest: to assign tasks detail to user automatically
		    It first try to find any free user (no task detail assigned) and not yet assigned task detail
			If not found, either user or task detail, it randomly select any user or task detail to assign.

4. A brief explanation of your design and approach
   About more detail about answers for this assignment please refer to 8seneca_Task assignment_ANSWER.xlsx file
   or online file: 
   https://docs.google.com/spreadsheets/d/1qghY4qz9eqOIuHN_iU7i4cyM5-w40dJANRmGZhH0XjU/edit?gid=0#gid=0
   
   Here, care about my brief explanation of your design and approach.
   Suppose it is a small DB (for demo), so tables contain maximum number of records is 2,147,483,647 (ID field type is int4)
   And DB relationship between entities (foreign key usage) is keep to easily navigate to extract information.
   Based on requirement DB model firtly clarify by:
		- users - tasks (relationship: 1 - n)
		- tasks_detail - tasks (relationship: 1 - n)   
   These relationship is relected in source code by using JPA hibernate annotation (once again suppose it is a small DB for demo!).
   
   APIs are designed to simple check valid input and perform requirement function 
   (assume that one task can be assigned to many users, but one task can not be assigned to someone twice).
   Standardize that HTTP content type of input is form-data type, and output is JSON.
   Output contains detail error code and data attached (if success). 
   DB is supposed to be small for demo, some features of API are ignored such as security, paging (such as for get all tasks, users API)!
   
   Moreover, requirement does not refer to concurrency control (such as update the same record simulteneously) 
   so update APIs are not to be applied this feature.

5. Support & co-operation
   The last but not least, because of lacking of time to investigate and discuss more about the assignment
   So perhaps existing misunderstanding point or something not yet perfect
   Please reach me if any request or help by:
	- Email: anphamvan@gmail.com
	- Mobile: +84 389 720 826   