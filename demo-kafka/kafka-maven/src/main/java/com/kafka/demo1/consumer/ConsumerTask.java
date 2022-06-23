package com.kafka.demo1.consumer;

import kafka.consumer.KafkaStream;
import kafka.consumer.ConsumerIterator;

public class ConsumerTask implements Runnable {

	private int m_threadNumber;

	private KafkaStream<byte[], byte[]> m_stream;

	public ConsumerTask(KafkaStream<byte[], byte[]> stream, int threadNumber) {
		m_threadNumber = threadNumber;
		m_stream = stream;
	}

	public void run() {
		System.out.println("-----Consumers begin to consume-------");

		ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
		while (it.hasNext()){
			System.out.println("Thread " + m_threadNumber + ": "+ new String(it.next().message()));
		}
		System.out.println("Shutting down Thread: " + m_threadNumber);
	}

}
