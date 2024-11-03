**Requirements:**
1. User can view books in library
   Scenario: As a User
   I want to see the books present in the library So that I can chose which book to borrow
   Given, there are no books in the library When, I view the books in the library Then, I see an empty library
   Given, there are books in the library When, I view the books in the library Then, I see the list of books in the
   library
2. User can borrow a book from the library
   Given, there are books in the library
   When, I choose a book to add to my borrowed list Then, the book is added to my borrowed list
   And, the book is removed from the library
   Note:
   a. Each User has a borrowing limit of 2 books at any point of time
3. User can borrow a copy of a book from the library
   Given, there are more than one copy of a book in the library When, I choose a book to add to my borrowed list
   Then, one copy of the book is added to my borrowed list And, the library has at least one copy of the book left
   Given, there is only one copy of a book in the library When, I choose a book to add to my borrowed list
   Then, one copy of the book is added to my borrowed list And, the book is removed from the library
   Note:
   a. Only 1 copy of a book can be borrowed by a User at any point of time
4. User can return books to the library
   Given, I have 2 books in my borrowed list
   When, I return one book to the library
   Then, the book is removed from my borrowed list And, the library reflects the updated stock of the book
   b. Given, I have 2 books in my borrowed list
   When, I return both books to the library
   Then, my borrowed list is empty
   And, the library reflects the updated stock of the books

**Tech Stack : Springboot with Java**
</br>
**Database : In-memory database (H2);**
</br>
</br>

**Implementation Details:**
</br>
1. Create a Springboot application with the above requirements
2. Implement the above requirements using RESTful APIs
3. Write test cases for the implemented APIs

**In Scope:**
1. Due to time constraints, I have added the logic in such a way that on load few books along with few users will be added to the library and for this mvp, the below are the limitations
2. The user can be only be one of the existing users added in [Users.json](./main/resources/Users.json). Hence to test the APIs, please use the users in [Users.json](./main/resources/Users.json)
3. The books can be only be one of the existing books added in [Books.json](./main/resources/Books.json). Hence to test the APIs, please use the books in [Books.json](./main/resources/Books.json)
4. APIs are written in such a way they use user name and phone number to identify the user and book id to identify the book

**Out of Scope:**
1. Exception handling 
2. Creation of user and books, adding books to library
3. Validations
4. Security and admin login features
