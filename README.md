# StarterProject

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Desktop icons

The desktop launcher uses the root `logo.png` for its window and system-tray icons.
On Windows it sets an explicit taskbar application identity before creating any UI
and shares the window icons with the native window class before showing the window.
The tray icon is removed when the game exits.

To package the Windows launcher, run `.\gradlew.bat :lwjgl3:packageWinX64`.
Extract the entire `lwjgl3\build\construo\dist\StarterProject-winX64.zip` and run
`StarterProject.exe`. The packaged executable uses `lwjgl3\icons\logo.png`;
update both logo files when changing the branding.

## Battle animation

Each successfully played attack card triggers a player wind-up, ram into the enemy, and
return to idle after the card flick. The player starts slowly, accelerates into
contact, then immediately bounces upward and eases back home.
Health and block labels stay in place.
Impact sounds play on contact; further card plays and ending the turn wait until
the player returns. A finishing hit completes its animation before the result screen opens.
Non-attack cards keep their existing effects and card flicks without triggering a ram.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
