package com.crossover.javaprogrammer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.test.exam.maker.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest(randomPort = true)
public class CrossoverSecondTest {

	private @Value("classpath:test1.txt") Resource resource;
	
	public @Test void testComparator() {
		try(InputStream in = resource.getInputStream()) {
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			Comparator comp=new Comparator();
	
			try {
					int T=Integer.parseInt(br.readLine().trim());
					while(T-->0){
						int ch=Integer.parseInt(br.readLine().trim());;
						if(ch==1)
						{
							String s1=br.readLine().trim();
							String s2=br.readLine().trim();
							if(comp.compare(s1,s2))
							{
								System.out.println("Same");
							}
							else
							{
								System.out.println("Different");
							}
						}
						else if(ch==2)
						{
							int num1=Integer.parseInt(br.readLine().trim());;
							int num2=Integer.parseInt(br.readLine().trim());;
							if(comp.compare(num1,num2))
							{
								System.out.println("Same");
							}
							else
							{
								System.out.println("Different");
							}
						}
						else if(ch==3)
						{
							String len=br.readLine().trim();
							String s1=br.readLine().trim();
							String s2=br.readLine().trim();
							StringTokenizer st=new StringTokenizer(len);
							StringTokenizer st1=new StringTokenizer(s1);
							StringTokenizer st2=new StringTokenizer(s2);
							int len1=Integer.parseInt(st.nextToken());
							int len2=Integer.parseInt(st.nextToken());
							
							int arr1[]=new int[len1];
							int arr2[]=new int[len2];
							for(int i=0;i<len1;i++)
							{
								arr1[i]=Integer.parseInt(st1.nextToken());
							}
							for(int i=0;i<len2;i++)
							{
								arr2[i]=Integer.parseInt(st2.nextToken());
							}
						
							if(comp.compare(arr1,arr2))
							{
								System.out.println("Same");
							}
							else
							{
								System.out.println("Different");
							}
							
						
						 }
						
							
					}
				
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			Assert.fail();
		}	
	}
}

class Comparator {
	public boolean compare(int a, int b) {
		return a == b;
	}
	
	public boolean compare(String a, String b) {
		return a == null? b == null: a.equals(b);
	}
	
	public boolean compare(int[] a, int[] b) {
		return Arrays.equals(a, b);
	}
}