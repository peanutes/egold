package com.zfhy.egold.common.util;


import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUtil {

	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	public static String RenameToUUID(String fileName) {
		return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
	}


	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\hh\\Downloads\\export_ZTE_YHMZB_201702\\export_ZTE_YHMZB_201702.txt");
		File outFile = new File("C:\\Users\\hh\\Downloads\\export_ZTE_YHMZB_201702\\top201709.csv");
		File rateOutFile = new File("C:\\Users\\hh\\Downloads\\export_ZTE_YHMZB_201702\\rate201709.csv");

		top(file, outFile);




	}

	private static void rate(File file, File outFile) throws IOException {
		FileUtils.writeLines(outFile, "GBK", Arrays.asList("月份,手机号,Http下载速率(MBPS)"), true);

		LineIterator it = FileUtils.lineIterator(file, "GBK");

		int lineNum = 0;
		int i = 0;
		List<String> lineList = Lists.newArrayListWithCapacity(10000);
		while(it.hasNext()){
			String line = it.nextLine();

			String[] items = line.split("\\|");

			Double rate = getDouble(items[33]) / 1024;

			if (rate < 1 && rate != 0) {
				String outline = String.join(",", "\"201709\"", "\"" + items[0] + "\"", "\"" + new DecimalFormat("0.00000").format(rate) + "\"");


				

				lineList.add(outline);


				lineNum++;
				System.out.println(">>>>" + lineNum);


				i++;

			}



			if (i == 10000) {
				FileUtils.writeLines(outFile, "GBK", lineList, true);
				lineList.clear();
				i = 0;
			}

		}

		if (CollectionUtils.isNotEmpty(lineList)) {
			FileUtils.writeLines(outFile, "GBK", lineList, true);
		}

	}

	private static void top(File file, File outFile) throws IOException {
		FileUtils.writeLines(outFile, "GBK", Arrays.asList("月份,手机号,top1,top2,top3,top4,top5,top1业务量(B),top2业务量(B),top3业务量(B),top4业务量(B),top5业务量(B),总流量(B)"), true);

		LineIterator it = FileUtils.lineIterator(file, "GBK");

		Double max = 0D;

		int lineNum = 0;
		int i = 0;
		List<String> lineList = Lists.newArrayListWithCapacity(10000);
		while(it.hasNext()){
			String line = it.nextLine();

			String[] items = line.split("\\|");

			TraffData traff1 = TraffData.builder().busId("15").traff(getDouble(items[47])).build();
			TraffData traff2 = TraffData.builder().busId("1").traff(getDouble(items[48])).build();
			TraffData traff3 = TraffData.builder().busId("7").traff(getDouble(items[49])).build();
			TraffData traff4 = TraffData.builder().busId("5").traff(getDouble(items[50])).build();
			TraffData traff5 = TraffData.builder().busId("8").traff(getDouble(items[51])).build();
			TraffData traff6 = TraffData.builder().busId("9").traff(getDouble(items[52])).build();
			TraffData traff7 = TraffData.builder().busId("6").traff(getDouble(items[53])).build();
			TraffData traff8 = TraffData.builder().busId("11").traff(getDouble(items[54])).build();
			TraffData traff9 = TraffData.builder().busId("4").traff(getDouble(items[55])).build();
			List<TraffData> traffDatas = Arrays.asList(traff1, traff2, traff3, traff4, traff5, traff6, traff7, traff8, traff9);
			double sum = traffDatas.stream().mapToDouble(TraffData::getTraff).sum();



			Collections.sort(traffDatas);
			traffDatas = traffDatas.subList(0, 5);






			String busIds = traffDatas.stream().map(e->e.getTraff()==0?"":e.getBusId()).collect(Collectors.joining(","));


			String busTraffs = traffDatas.stream().map(TraffData::getTraff).map(e->"\""+DoubleUtil.toString(e)+"\"").collect(Collectors.joining(","));



			String outline = String.join(",", "\"201709\"", "\""+items[0]+"\"", busIds, busTraffs, "\"" + DoubleUtil.toString(sum) + "\"");




			if (sum > max) {
				max = sum;
			}

			if (sum != 0) {
				lineList.add(outline);
				i++;
			}


			lineNum++;
			System.out.println(">>>>" + lineNum);


			if (i == 10000) {
				FileUtils.writeLines(outFile, "GBK", lineList, true);
				lineList.clear();
				i = 0;
			}

		}

		if (CollectionUtils.isNotEmpty(lineList)) {
			FileUtils.writeLines(outFile, "GBK", lineList, true);
		}

		System.out.println("max sum >>>" + max);

	}


	private static Double getDouble(String item) {
		if (StringUtils.isBlank(item)) {
			return 0D;
		}


		return new Double(StringUtils.trim(item));
	}


}
