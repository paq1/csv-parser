ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

val baseName = "csv-parser"

lazy val root = (project in file("modules/api"))
  .dependsOn(core)
  .settings(
    name := s"$baseName-api"
  )

lazy val core = (project in file("modules/core"))
  .settings(
    name := s"$baseName-core",
    libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.12"
  )
