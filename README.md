# WAI
Where Am I App

## How to use git?
If you prefer GUIs try one of these:

SmartGit for Linux http://www.syntevo.com/smartgit/

GitHub for Windows https://windows.github.com/

GitHub for Mac https://mac.github.com/

or http://stackoverflow.com/a/1518844

You can also use terminal if you like to.

### How to clone this project to my machine?
Open terminal, switch to a location where you want this project to be cloned to and type:
```
git clone https://github.com/bernhardfritz/WAI.git
```

### How to push files to git?
Open terminal, switch to project directory and type:
```
git add .
git commit -m "Some information about this commit"
git push
```

### How to pull files from git?
```
git pull
```

### Some additional information about git
If you're not able to push to git, it's likely that someone else has pushed something to the repository while you were working. In this case pull first, if needed merge conflicts and then add, commit and push again.

Take your time merging!

Do only commit working code!

## How to set up our development environment for the first time?
Install Virtual Box https://www.virtualbox.org/

Install Vagrant https://www.vagrantup.com/

Open terminal, switch to project directory and type:
```
vagrant up
vagrant ssh
sudo apt-get update
sudo apt-get install openjdk-7-jdk
cd /vagrant
cd activator-1.3.2-minimal
./activator
export PATH=$PATH:/vagrant/activator-1.3.2-minimal
cd /vagrant/play-java
activator run
```

## How to start play-framework in vagrant once it's set up?
Open terminal, switch to project directory and type:
```
vagrant up
vagrant ssh
cd /vagrant/play-java
activator
run
```
Press Ctrl+D once to restart the server
## How to display our webapp?
Open your favorite browser and go to http://localhost:9000/

## How to stop play-framework and vagrant?
To stop play-framework press Ctrl+D twice

To stop vagrant ssh type: `exit`

To stop vagrant type: `vagrant suspend`

## Important notice
Do only start play-framework inside vagrant!
