# CS3398--Maggia--F2019

# Chess++

> This will be our redesigned chess game that will include additional pieces, goals, unlocks, as well as extended boards. We will begin by making the standard game of chess at first. Eventually, our team, Guillermo Gomez, Roy Grady, Kody Davis, Felipe Rodriguez, and Kieran Hsieh, will add more pieces, spaces, goals, and achievements in-game. Our goal is to test our own personal skills by creating something similar yet different. This project is for chess enthusiasts who want some variety in their gameplay. We hope to create an interesting and dynamic chess game that will be user friendly, challenging, fun, and unique.  

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)
* [Inspiration](#inspiration)
* [Contact](#contact)
* [Sprint 1 Review](#Sprint-1-Review)

## General info

> We wish we had the knowledge and experience to implement this game into a web browser with online functionality.

## Screenshots
![Example screenshot](https://i.pinimg.com/736x/98/7b/0b/987b0b6fcc6987ada88a448b2ddbe10a--cool-wallpaper-chess.jpg)

## Technologies
* Tech 1 - JRE version 1.8
* Tech 2 - version 2.0
* Tech 3 - version 3.0

## Setup
Describe how to install / setup your local environement / add link to demo version.

## Code Examples
Show examples of usage:
`put-your-code-here`

## Features
List of features ready and TODOs for future development       
* -

To-do list:
* Fundamental movement/position mechanics of the chess engine 

  -        This lays the foundations of the game so that the base rules of chess are established.
           This feature is vital for any playability and necessary for most future features.
           It corresponds with the story on offline playability.
           
* Implementing classic chess pieces 

  -        Chess pieces are another necessary component of chess playability.
           This feature assigns valid movement options to each chess piece.
           This feature is a prerequisite for the user stories on new and customizable chess pieces.
           
* Menus 

  -        Menus allow users to navigate settings, modes, and other options.
           A successful implementation will be user friendly and visually appealing.
           This corresponds with the user story on intuitive visual interfaces.
           
* Player info 

  -        This feature stores player data such as username, match history, rank, etc...
           It will be used by frequent users and features that rely on metadata.
           This feature corresponds to the user stories on competitive ranking systems.
           
* Board graphics and chess piece sprites 

  -        Basic graphic designs for each essential component of chess.
           The first step in creating an interactive graphic interface for players.
           This also corresponds to the user story on visual interfaces.
           
## Status
Project is: in progress. Sprint 1 is complete.

## Inspiration
Add here credits. Project inspired by..., based on...

## Contact
Created by [Guillermo Gomez](g_g224@txstate.edu) 
           [Roy Grady](rag189@txstate.edu)
           [Felipe Rodriguez](f_r95@txstate.edu)
           [Kody Davis](kody_davis@txstate.edu)
           [Kieran Hsieh](kth43@txstate.edu)

- feel free to contact us!

## Sprint 1 Review

* Roy Grady

  -        I created the code for the board representation as well as all of the computations for
           the possible moves the pieces can make. I also managed how the board recognizes where
           the pieces are and the conditions for absolute pins, interference squares, and checkmate.
           My code in GitHub is everything in Board.java, WhitePawn.java, WhiteBishop.java,
           WhiteKnight.java, WhiteRook.java, WhiteQueen.java, WhiteKing.java, BlackPawn.java, 
           BlackBishop.java, BlackKnight.java, BlackRook.java, BlackQueen.java, BlackKing.java, 
           and PawnPromotion.java. The main branch I used when updating my code was in RoyTemp.
           
* Kieran Hsieh

  -        Completed the chess GUI, which can be found in ChessGUI.java under the Board branch. This was used to visually represent the            Board, and was used in the Menu as something that opened after the play button was clicked.
           
* Guillermo Gomez

  -        I created the basic main menu interface for the chess game. I implemented the standard play button as well as other optional            buttons such as settings and profile. This menu can be found under the Menu.java class, using the Button class to create the            buttons in the menu. All these classes are under the Menu branch. 
           
* Kody Davis

  -        I helped with the construction of the GUI by finding pictures of chess pieces as well as a way
           to read them in and show them on the board. I also added the menu bar with the options button on top
           of the board as a way to change the color of the board for player customization. Only one color can
           be selected at a time and one color must always be selected. The branch I used to track my changes is
           called KodyGUI.
           
* Felipe Rodriguez

  -        The user profile has been completed as a constructor that will inititate the players ELO score along 
           all other information such as name, username, date of birth, etc.
