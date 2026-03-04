package org.duhyeok.dto;

public class MatchResponseDto {
	private String teamA;
	private String teamB;
	private int teamAPosCnt;
	private int teamBNegCnt; 
	private double winProbability;
	private String predictedWinner;

	
	
	
	
	public MatchResponseDto() {}

	public MatchResponseDto(String teamA, String teamB, int teamAPosCnt, int teamBNegCnt, double winProbability,
			String predictedWinner) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.teamAPosCnt = teamAPosCnt;
		this.teamBNegCnt = teamBNegCnt;
		this.winProbability = winProbability;
		this.predictedWinner = predictedWinner;
	}

	// Getter 메서드들 (롬복 @Getter 대체)
	public String getTeamA() {
		return teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public int getTeamAPosCnt() {
		return teamAPosCnt;
	}

	public int getTeamBNegCnt() {
		return teamBNegCnt;
	}

	public double getWinProbability() {
		return winProbability;
	}

	public String getPredictedWinner() {
		return predictedWinner;
	}
}