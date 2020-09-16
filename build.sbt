import sbtwelcome._

enablePlugins(ScalaJSBundlerPlugin)

name := "dio-geo"

scalaVersion := "2.13.3"

npmDependencies in Compile += "react"       -> "16.13.1"
npmDependencies in Compile += "react-dom"   -> "16.13.1"
npmDependencies in Compile += "react-proxy" -> "1.1.8"

npmDevDependencies in Compile += "file-loader"         -> "6.0.0"
npmDevDependencies in Compile += "style-loader"        -> "1.2.1"
npmDevDependencies in Compile += "css-loader"          -> "3.5.3"
npmDevDependencies in Compile += "html-webpack-plugin" -> "4.3.0"
npmDevDependencies in Compile += "copy-webpack-plugin" -> "5.1.1"
npmDevDependencies in Compile += "webpack-merge"       -> "4.2.2"

libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.6"
libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.6.6"

scalacOptions += "-Ymacro-annotations"

version in webpack := "4.43.0"
version in startWebpackDevServer := "3.11.0"

webpackResources := baseDirectory.value / "webpack" * "*"

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js")
webpackConfigFile in Test := Some(baseDirectory.value / "webpack" / "webpack-core.config.js")

webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot")
webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly()

requireJsDomEnv in Test := true

addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS")
addCommandAlias("build", "fullOptJS::webpack")
addCommandAlias("fmt", "all root/scalafmtSbt root/scalafmtAll")
addCommandAlias("fmtCheck", "all root/scalafmtSbtCheck root/scalafmtCheckAll")

logo :=
  s"""
     |████████▄   ▄█   ▄██████▄          ▄██████▄     ▄████████  ▄██████▄
     |███   ▀███ ███  ███    ███        ███    ███   ███    ███ ███    ███
     |███    ███ ███▌ ███    ███        ███    █▀    ███    █▀  ███    ███
     |███    ███ ███▌ ███    ███       ▄███         ▄███▄▄▄     ███    ███
     |███    ███ ███▌ ███    ███      ▀▀███ ████▄  ▀▀███▀▀▀     ███    ███
     |███    ███ ███  ███    ███        ███    ███   ███    █▄  ███    ███
     |███   ▄███ ███  ███    ███        ███    ███   ███    ███ ███    ███
     |████████▀  █▀    ▀██████▀         ████████▀    ██████████  ▀██████▀
     |
     |""".stripMargin

usefulTasks := Seq(
  UsefulTask("a", "dev", "Start the webapp at localhost:8080 with hot reloading enabled"),
  UsefulTask("b", "build", "Build a fully optimized JS file"),
  UsefulTask("c", "fmt", "Run scalafmt on the entire project"),
)
