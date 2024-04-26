import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
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
        for (Map<String,Double> M: map.values()) {
            // 检查输入的站点是否在当前线路中
            ArrayList<String> stations = new ArrayList<String>() ;
for(String i:M.keySet())
{stations.add(i);
}


            if (stations.contains(station)) {
                int index = stations.indexOf(station);
                // 在站点前后遍历以找到与输入站点相隔 `n` 站以内的所有站点
                for (int i = Math.max(0, index - distance); i <= Math.min(stations.size() - 1, index + distance); i++) {
                    // 计算相隔的站点数量
                    int distanceFromStation = Math.abs(i - index);
                    // 构建结果字符串并添加到结果列表中
                    results.add(String.format("<<%s.%d>>", stations.get(i), distanceFromStation));
                }
            }
        }
        return results;
    }




}

