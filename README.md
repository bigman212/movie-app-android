# movie-app (old from RMR Gitlab: internship_android_levinkov)
### Dependencies
[Here](buildSrc/src/main/java/ru/redmadrobot/movie_app/Dependencies.kt)

### Project structure
Whole project consists of next modules:
- **:app** main module with Application.
- **:common** has common classes, interfaces that are used mainly everywhere in the project (e.g. BaseFragment or BaseViewModel).
- **:core**
  - :network code for communicating with the server;
  - :android code for creating, injecting, and managing Android-specific logic (e.g. SharedPreferences);
  - :persist code for persisting data using Room.
- **:feature**
  - :auth login with MovieDb credentials or using pin-code
  - :movie_list list of movies fetched from server, favorite movies list
  - :movie_detail more detailed info of clicked movie, movie can be added to favorites
  - :profile logout, profile info
- **:test_tools** mocks and common classes for testing

Submodules of :feature is structured by Clean Architecture *(UI -> ViewModel -> UseCase -> Repository -> Data source)*.

### Git flow and commit naming
Naming is more [Git Flow](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html), but with a few changes from me.  
Commit naming:
```bash
(type/<module_name_optional>) commit title in 80 symbols or less
```
Possible types:
- dev - coding without any big feature changes in the app for user (e.g. create api client);
- feat - feature requirements complex implementation (e.g. auth user from credentials when login)
- di - any dependency injection change or addition in modules or components;
- refactor - refactor :D;
- fix - any crash or bug fix;
- docs - comments, docs;
- design - changes in visual side of the app;

It is possible to write module name where changes were made:
```bash
(feat/auth) auth user using pin-code
```

