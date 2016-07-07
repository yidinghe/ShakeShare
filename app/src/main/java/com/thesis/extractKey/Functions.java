package com.thesis.extractKey;

import java.util.ArrayList;
import java.util.List;

public class Functions {
	
	public static List<List<Float>> equalSplitArrayList( ArrayList<Float> mDataEntropy, int n){
		int N = mDataEntropy.size();
		List<List<Float>> segments = new ArrayList<List<Float>>();
		for(int i=1; i*n<=N; i++)
			segments.add(mDataEntropy.subList((i-1)*n, i*n));
		return segments;
	}
	
	public static List<ArrayList<Float>> arithmaticSeriesSegments3D ( ArrayList<Float> mDataEntropy){
		List<ArrayList<Float>> segments = new ArrayList<ArrayList<Float>>();
		ArrayList<Float> xAxis = new ArrayList<Float>();
		ArrayList<Float> yAxis = new ArrayList<Float>();
		ArrayList<Float> zAxis = new ArrayList<Float>();
		int N = mDataEntropy.size();
		for( int i=0; i<=N/3; i++){
			xAxis.add(mDataEntropy.get((i-1)*3));
			yAxis.add(mDataEntropy.get((i-1)*3)+1);
			zAxis.add(mDataEntropy.get((i-1)*3)+2);		
		}
		segments.add(xAxis);
		segments.add(yAxis);
		segments.add(zAxis);
		return segments;
	}
	
}
