# CricketMatch

## Added Db functionality
* Connection made to mysql using `com.mysql.cj.jdbc` library
* Server used `localhost:3306/sys`



### Database tables:
 * Team - Stores team names uniqely and generates team_id for other tables
 
 
 
 * CricketMatch - stores result of match with team names,team id's, team scores, team wickets
 
 
 
 * PlayerDetails - Stores details of every player batsman or bowler with runs, strike rate, economy rate, overs_taken, runs_given..etc
 
 
 
 * BallData - stores details of every ball that is being played, with batsman name, bowler name and runs made or wicket taken
 
 
 
