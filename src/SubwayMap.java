import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class SubwayMap {
    private Map<String, Map<String, Double>> map;


    public SubwayMap() {
        this.map = new LinkedHashMap<>();
    }


    public void addLine(String lineName) {
        map.put(lineName, new LinkedHashMap<>());
    }

    public void addStation(String lineName, String stationName, double distance) {
        map.get(lineName).put(stationName, distance);
    }

    public double getDistance(String lineName, String station1, String station2) {
        return map.get(lineName).get(station1) + map.get(lineName).get(station2);
    }

    public Set<String> getTransferStations() {
        Map<String, Set<String>> stationLines = new HashMap<>();
        for (String line : map.keySet()) {
            for (String station : map.get(line).keySet()) {
                stationLines.putIfAbsent(station, new HashSet<>());
                stationLines.get(station).add(line);
            }
        }

        Set<String> transferStations = new HashSet<>();
        for (String station : stationLines.keySet()) {
            if (stationLines.get(station).size() > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("<").append(station).append(", <");
                for (String line : stationLines.get(station)) {
                    sb.append(line).append(" 号线、");
                }
                sb.setLength(sb.length() - 1); // Remove the last comma
                sb.append(">>");
                transferStations.add(sb.toString());
            }
        }

        return transferStations;
    }


    public String toString() {
        return this.map.values().toString();
    }

    public List<String> findStationsWithinDistance(String station, int distance) {
        List<String> results = new ArrayList<>();
        // 遍历每一条地铁线路
        List<String> Line = new ArrayList<>();
        for (String i : map.keySet()) {
            Line.add(i);
        }
        int t = 0;
        for (Map<String, Double> M : map.values()) {
            // 检查输入的站点是否在当前线路中

            String line = Line.get(t);
            ArrayList<String> stations = new ArrayList<String>();
            for (String i : M.keySet()) {
                stations.add(i);
            }


            if (stations.contains(station)) {


                int index = stations.indexOf(station);
                // 在站点前后遍历以找到与输入站点相隔 `n` 站以内的所有站点
                for (int i = Math.max(0, index - distance); i <= Math.min(stations.size() - 1, index + distance); i++) {
                    // 计算相隔的站点数量
                    int distanceFromStation = Math.abs(i - index);
                    // 构建结果字符串并添加到结果列表中

                    results.add("<<" + stations.get(i) + "," + line + "号线" + "," + distanceFromStation + ">>");
                }
            }
            t = t + 1;
        }
        return results;
    }


    public ArrayList<List<String>> findAllPaths(String startStation, String endStation) {
        ArrayList<List<String>> allPaths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        currentPath.add(startStation);
        findAllPathsDFS(startStation, endStation, visited, currentPath, allPaths);
        return allPaths;
    }

    private void findAllPathsDFS(String currentStation, String endStation, Set<String> visited, List<String> currentPath, List<List<String>> allPaths) {
        visited.add(currentStation);
        // 如果当前站点是终点站，则将当前路径添加到结果集中
        if (currentStation.equals(endStation)) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            // 否则，遍历当前站点相连的所有站点
            List<String> connectedStations = getConnectedStations(currentStation);
            for (String nextStation : connectedStations) {
                if (!visited.contains(nextStation)) {
                    currentPath.add(nextStation);
                    findAllPathsDFS(nextStation, endStation, visited, currentPath, allPaths);
                    currentPath.remove(currentPath.size() - 1); // 回溯
                }
            }
        }
        visited.remove(currentStation);
    }

    private List<String> getConnectedStations(String station) {
        List<String> connectedStations = new ArrayList<>();

        for (Map<String, Double> M : map.values()) {
            // 检查输入的站点是否在当前线路中

            ArrayList<String> stations = new ArrayList<String>();
            for (String i : M.keySet()) {
                stations.add(i);
            }


            if (stations.contains(station)) {
                int index = stations.indexOf(station);
                if (index > 0) {
                    connectedStations.add(stations.get(index - 1)); // 添加前一个站点
                }
                if (index < stations.size() - 1) {
                    connectedStations.add(stations.get(index + 1)); // 添加后一个站点
                }
            }
        }
        return connectedStations;
    }

    public void test3() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入起点站名称：");
        String startStation = scanner.nextLine();
        System.out.print("请输入终点站名称：");
        String endStation = scanner.nextLine();

        List<List<String>> allPaths = this.findAllPaths(startStation, endStation);
        if (allPaths.isEmpty()) {
            System.out.println("未找到起点站和终点站之间的路径。");
        } else {
            System.out.println("连接起点站和终点站的所有路径为：");
            for (List<String> path : allPaths) {
                System.out.println(path);
            }
        }

        scanner.close();
    }
}