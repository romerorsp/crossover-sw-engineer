package com.crossover.javaprogrammer.test;

import java.io.IOException;
import java.util.Scanner;

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
public class CrossoverTest {

	private @Value("classpath:test2.txt") Resource resource;
	
	public @Test void testScanner() {
		try (Scanner scanner = new Scanner(resource.getInputStream())) {
			myCalculator calculator = new myCalculator();
			do {
				String line = scanner.nextLine();
				if(line == null || line.length() < 1) {
					System.exit(0);
				}
				String[] entries = line.split(" ");
				int n = Integer.parseInt(entries[0]),
						p = Integer.parseInt(entries[1]);
				try {
					System.out.println(calculator.power(n, p));
				} catch(Exception e) {
					System.out.println(e);
				}
			} while(scanner.hasNextLine());
		} catch (IOException e) {
			Assert.fail();
		}
	}	
}

class myCalculator{

	int power(int n,int p) throws Exception
	{
		if(n<0 || p<0) throw new Exception("n and p should be non-negative");
		if(p==0) return 1;
		return n*power(n,p-1);
	}
}