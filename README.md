# Darker Notes
This repository contains all the pertinent information on our Markdown and LaTeX Note Editor. Our team is composed of: Alexander Gednov,
Connor Buckley, Hriday Panchasara, Laurence Fong, and Tong Wu. See the appropriate documents for details on the different
parts of our project.

## Instructions

### Set up with Eclipse

1. Open Terminal
2. Change directory to your eclipse workspace
  1. Typically at `cd ~/eclipse-workspace`
1. Clone the repository in your eclipse workspace directory `git clone git@github.com:Laurenzarus/DarkerNotes.git`
2. Open Eclipse
3. Go to `File -> Open Projects From File System...`
4. Import source from the directory you cloned, the folder called `final-project`
5. Click Finish
6. success!

### Database

To make development easier, change your MySQL root user password to `password`

#### Instructions if you need to change your password

1. On macOS, change directory to the MySQL command line utility
  1. `cd /usr/local/mysql-8.0.12-macos10.13-x86_64/bin` (might be different depending on your MySQL version)
1. `./mysql -u root -p`
2. Enter your current password
3. Once you are in, it should look like `mysql> `
4. Enter `FLUSH PRIVILEGES;`
5. Enter `ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';`
6. Enter `\q` to exit
7. You can make sure it works by repeating steps 3 and 4 to see if you can log in successfully

reference: [https://dev.mysql.com/doc/refman/8.0/en/resetting-permissions.html](https://dev.mysql.com/doc/refman/8.0/en/resetting-permissions.html)

#### Setting up the Database

Open `makeDatabase.sql` in the root directory of the project.

In MySQL Workbench, click the lightning button and it should create the database! You are then ready to run Tomcat and use the app.


## Dependencies

### Client-side

* [jQuery](https://code.jquery.com/)
* [Bootstrap](http://getbootstrap.com/)
* [Showdown](http://showdownjs.com/), a Javascript markdown parser
* [KaTeX](https://katex.org/), a fast LaTeX parser/renderer written in Javascript
	* View API [here](https://katex.org/docs/api.html) for usage


### Server-side

* Java Servlets
* JDBC
* MySQL
