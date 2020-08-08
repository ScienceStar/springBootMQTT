package bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.*;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author lxc
 * @Date 2020/4/22 14:29
 * @Version V1.0
 **/
public class Test {

    private static int size = 1000000;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);

    public static void main(String[] args) {
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        List<Integer> list = new ArrayList<Integer>(1000);
        //故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 10000; i < size + 20000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("误判的数量：" + list.size());
    }

    @org.junit.Test
    public void testSwap() {
        int a = 3, b = 4;
        a += b;
        b = a - b;
        a -= b;
        System.out.println(a + "    " + b);

        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a + "    " + b);
    }

    @org.junit.Test
    public void testStream() throws FileNotFoundException {
        List<String> nameList = Arrays.asList("mike", "tina", "jim", "jack", "bruce");
        nameList.stream().filter(name ->
                name.length() == 4
        ).map(name -> "This is:" + name)
                .forEach(name -> {
                    System.out.println(name);
                });

        List<String> upperCaseNameList = nameList.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        upperCaseNameList.forEach(name -> System.out.println(name + ","));

        System.out.println("==================流的获取===========================");
        String[] nameArr = {"Darcy", "Chris", "Linda", "Sid", "Kim", "Jack", "Poul", "Peter"};
        // 集合获取 Stream 流
        Stream<String> nameListStream = nameList.stream();
        // 集合获取并行 Stream 流
        Stream<String> nameListStream2 = nameList.parallelStream();
        // 数组获取 Stream 流
        Stream<String> nameArrStream = Stream.of(nameArr);
        // 数组获取 Stream 流
        Stream<String> nameArrStream1 = Arrays.stream(nameArr);
        // 文件流获取 Stream 流
        BufferedReader bufferedReader = new BufferedReader(new FileReader("README.md"));
        Stream<String> linesStream = bufferedReader.lines();
        // 从静态方法获取流操作
        IntStream rangeStream = IntStream.range(1, 10);
        rangeStream.limit(10).forEach(num -> System.out.print(num + ","));
        System.out.println();
        IntStream intStream = IntStream.of(1, 2, 3, 3, 4);
        intStream.forEach(num -> System.out.print(num + ","));
        System.out.println("====================for each=============================");
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        numberList.stream().forEach(number -> System.out.println(number + ","));

        System.out.println("=================map=======================");
        List<Integer> numberList2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // 映射成 2倍数字
        List<Integer> collect = numberList2.stream()
                .map(number -> number * 2)
                .collect(Collectors.toList());
        collect.forEach(number -> System.out.print(number + ","));
        System.out.println();

        numberList2.stream()
                .map(number -> "数字 " + number + ",")
                .forEach(number -> System.out.println(number));

        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        List<Integer> collect2 = inputStream
                .flatMap((childList) -> childList.stream())
                .collect(Collectors.toList());
        collect2.forEach(number -> System.out.print(number + ","));
    }

    @org.junit.Test
    public void testFilter() {
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> collect = numberList.stream()
                .filter(number -> number % 2 == 0)
                .collect(Collectors.toList());
        collect.forEach(number -> System.out.print(number + ","));
    }

    @org.junit.Test
    public void testOptional() {
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Optional<Integer> firstNumber = numberList.stream()
                .findFirst();
        System.out.println(firstNumber.orElse(-1));
    }

    @org.junit.Test
    public void testCollect() {
        List<Integer> numberList = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5);
        // to array
        Integer[] toArray = numberList.stream()
                .toArray(Integer[]::new);
        // to List
        List<Integer> integerList = numberList.stream()
                .collect(Collectors.toList());
        // to set
        Set<Integer> integerSet = numberList.stream()
                .collect(Collectors.toSet());
        System.out.println(integerSet);
        // to string
        String toString = numberList.stream()
                .map(number -> String.valueOf(number))
                .collect(Collectors.joining()).toString();
        System.out.println(toString);
        // to string split by ,
        String toStringbJoin = numberList.stream()
                .map(number -> String.valueOf(number))
                .collect(Collectors.joining(",")).toString();
        System.out.println(toStringbJoin);
    }

    @org.junit.Test
    public void testLimit() {
// 生成自己的随机数流
        List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
        ageList.stream()
                .limit(3)
                .forEach(age -> System.out.print(age + ","));
        System.out.println();

        ageList.stream()
                .skip(3)
                .forEach(age -> System.out.print(age + ","));
    }

    @org.junit.Test
    public void testStatistics() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        IntSummaryStatistics stats = list.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("最小值：" + stats.getMin());
        System.out.println("最大值：" + stats.getMax());
        System.out.println("个数：" + stats.getCount());
        System.out.println("和：" + stats.getSum());
        System.out.println("平均数：" + stats.getAverage());
    }

    @org.junit.Test
    public void testGroupingBy() {
        List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
        Map<String, List<Integer>> ageGrouyByMap = ageList.stream()
                .collect(Collectors.groupingBy(age -> String.valueOf(age / 10)));
        ageGrouyByMap.forEach((k, v) -> {
            System.out.println("年龄" + k + "0多岁的有：" + v);
        });
    }

    @org.junit.Test
    public void testPartitioningBy() {
        List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
        Map<Boolean, List<Integer>> ageMap = ageList.stream()
                .collect(Collectors.partitioningBy(age -> age > 18));
        System.out.println("未成年人：" + ageMap.get(false));
        System.out.println("成年人：" + ageMap.get(true));
    }

    @org.junit.Test
    public void generateTest() {
// 生成自己的随机数流
        Random random = new Random();
        Stream<Integer> generateRandom = Stream.generate(random::nextInt);
        generateRandom.limit(5).forEach(System.out::println);
        // 生成自己的 UUID 流
        Stream<UUID> generate = Stream.generate(UUID::randomUUID);
        generate.limit(5).forEach(System.out::println);
    }

    @org.junit.Test
    public void lazyTest() {
// 生成自己的随机数流
        List<Integer> numberLIst = Arrays.asList(1, 2, 3, 4, 5, 6);
        // 找出偶数
        Stream<Integer> integerStream = numberLIst.stream()
                .filter(number -> {
                    int temp = number % 2;
                    if (temp == 0) {
                        System.out.println(number);
                    }
                    return temp == 0;
                });

        System.out.println("分割线");
        List<Integer> collect = integerStream.collect(Collectors.toList());
    }

    @org.junit.Test
    public void testCaculate() {
// 生成自己的随机数流，取一千万个随机数
        Random random = new Random();
        Stream<Integer> generateRandom = Stream.generate(random::nextInt);
        List<Integer> numberList = generateRandom.limit(10000000).collect(Collectors.toList());

        // 串行 - 把一千万个随机数，每个随机数 * 2 ，然后求和
        long start = System.currentTimeMillis();
        int sum = numberList.stream()
                .map(number -> number * 2)
                .mapToInt(x -> x)
                .sum();
        long end = System.currentTimeMillis();
        System.out.println("串行耗时：" + (end - start) + "ms，和是:" + sum);

        // 并行 - 把一千万个随机数，每个随机数 * 2 ，然后求和
        start = System.currentTimeMillis();
        sum = numberList.parallelStream()
                .map(number -> number * 2)
                .mapToInt(x -> x)
                .sum();
        end = System.currentTimeMillis();
        System.out.println("并行耗时：" + (end - start) + "ms，和是:" + sum);
    }
}
