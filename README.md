# MovieApp

My stack:
- View Binding to bind the view to the code
- Clean Architecture to better organize the code and make it more flexible and scalable
- Coroutines for asynchronous operations
- DI Koin for managing dependencies in the application
- Retrofit 2 for API usage
- Recycler View
- Coil for upload images
- Movie API: https://www.omdbapi.com/


## Application structure.

The application is divided into three layers of architecture: 

- **presentation**: contains the View and Presenter, which are responsible for displaying and managing the user interface
- **domain**: contains the Use Case interfaces, responsible for application activity and fetching data from the server
- **data**: contains the implementation of the data, including the network layer, database and data source with the movie API https://www.omdbapi.com/

## Screenshots
<img src = "https://github.com/Xek0mb1k/MovieApp/assets/84407094/f9478244-76c3-4c98-abb9-8c2d9e5b2ec8" width="300">
<img src = "https://github.com/Xek0mb1k/MovieApp/assets/84407094/b99dd68f-bcfa-41a0-9ea3-c1eb433352c6" width="300">
<img src = "https://github.com/Xek0mb1k/MovieApp/assets/84407094/0d4fc854-8477-4c79-a223-dc34fea11c47" width="300">
