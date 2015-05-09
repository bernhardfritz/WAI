name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "com.google.guava" % "guava" % "18.0"

libraryDependencies += "javax.mail" % "mail" % "1.4"

libraryDependencies += "net.coobird" % "thumbnailator" % "[0.4, 0.5)"