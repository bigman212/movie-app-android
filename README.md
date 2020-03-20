# internship_android_levinkov
### Зависимости
Можно посмотреть [здесь](buildSrc/src/main/java/ru/redmadrobot/movie_app/Dependencies.kt)

### Структура проекта
Проект разделен на подмодули:
- **:app** главный модуль с Application классом;
- **:common** содержит классы и интерфейсы которые используются в проекте почти везде. Например BaseViewModel;
- **:core**
  - :network логика для взаимодействия с сервером;
  - :android логика инициализации и внедрения Android специфичных классов;
- **:feature**
  - :auth экран авторизации и все что с ним связано;
- **...WIP... **

Подмодули :feature в свою очередь структурированы согласно Clean Architecture *(UI -> ViewModel -> UseCase -> Repository -> Data source)*.

### Работа с Git
Нейминг коммитов старается быть похожим на [Git Flow](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html), но с небольшими изменениями.  
Коммиты в заголовке пишутся по правилу
```bash
(тип/<опционально_модуль_с_изменениями>) заголовок для коммита в 80 символов
```
Возможные типы:
- dev - разработка проекта без изменений касательно функционала "создан клиент для апи запросов";
- feat - изменения которые реализуют функциональные требования, например "отправка запроса на авторизацию при нажатии на кнопку"
- di - изменения касающиеся внедрения зависимостей, а именно изменения в компонентах, модулях и т.д.;
- refactor - собсна сабж;
- fix - исправление какой-либо ошибки, вылета;
- docs - документация, комментарии к коду;
- design - правки в верстке, стилях, дизайне;

В заголовке возможно может указан модуль в котором происходили изменения. Например:
```bash
(feat/auth) авторизация используя пин-код
```

