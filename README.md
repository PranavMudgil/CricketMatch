# CricketMatch

## Added Db functionality
* Connection made to mysql using `com.mysql.cj.jdbc` library
* Server used `localhost:3306/sys`



### Database tables:
 * Team - Stores team names uniqely and generates team_id for other tables
 
 <img width="168" alt="Screenshot 2019-07-01 at 1 54 02 PM" src="https://user-images.githubusercontent.com/24803889/60423120-1dcc1c80-9c0b-11e9-8679-5839fb3782fa.png">
 
 
 * CricketMatch - stores multiple results of match with team names,team id's, team scores, team wickets each time the program is run.
 
 <img width="830" alt="Screenshot 2019-07-01 at 1 52 22 PM" src="https://user-images.githubusercontent.com/24803889/60423165-33d9dd00-9c0b-11e9-823d-4043b54f4af3.png">
 
 * PlayerDetails - Stores details of every player batsman or bowler with runs, strike rate, economy rate, overs_taken, runs_given..etc
 
 <img width="1201" alt="Screenshot 2019-07-01 at 1 53 36 PM" src="https://user-images.githubusercontent.com/24803889/60423193-43592600-9c0b-11e9-91ae-06accda81e81.png">

 
 * BallData - stores details of every ball that is being played, with batsman name, bowler name and runs made or wicket taken
 
 
 <img width="413" alt="Screenshot 2019-07-01 at 1 55 14 PM" src="https://user-images.githubusercontent.com/24803889/60423000-ce85ec00-9c0a-11e9-9598-64642c0d4f1f.png">
