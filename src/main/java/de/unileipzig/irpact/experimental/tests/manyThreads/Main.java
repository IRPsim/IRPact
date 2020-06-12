package de.unileipzig.irpact.experimental.tests.manyThreads;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.experimental.ExperimentalIgnorePrintStream;
import de.unileipzig.irpact.experimental.ExperimentalUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.time.LocalTime;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Main {

    private static String getPackageName() {
        return Main.class.getPackage().getName();
    }

    private static CreationInfo newAgent(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".ThreadAgent.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo[] newAgents(int count, String name, int start) {
        CreationInfo[] infos = new CreationInfo[count];
        for(int i = 0; i < count; i++) {
            infos[i] = newAgent(name + (start + i));
        }
        return infos;
    }

    private static void log(String msg) {
        System.out.println("[" + LocalTime.now() + "] [main] " + msg);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ExperimentalIgnorePrintStream.redirectSystemOut();

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        ISimulationService simulationService = ExperimentalUtil.getSimulationService(platform);
        simulationService.pause().get();

        log("start agents...");
        List<IExternalAccess> agents = new ArrayList<>();
        int batches = 30;
        int perBatch = 500; //ab 1000 stackoverflow
        long start = System.currentTimeMillis();
        for(int i = 0; i < batches; i++) {
            /*
            for(int j = 0; j < perBatch; j++) {
                CreationInfo info = newAgent("name#" + (i * perBatch + j));
                IExternalAccess agent = platform.createComponent(info).get();
                agents.add(agent);
            }
            */
            CreationInfo[] infos = newAgents(perBatch, "name#", perBatch * i);
            Collection<IExternalAccess> agentColl = platform.createComponents(infos)
                    .get();
            agents.addAll(agentColl);
            log("batch '" + i + "' started");
        }
        log("...agents started: " + (System.currentTimeMillis() - start));

        simulationService.start().get();

        log("wait 10sek");
        ConcurrentUtil.sleepSilently(10000);
        log("kill");
        ConcurrentUtil.sleepSilently(1000);

        long killStart = System.currentTimeMillis();
        platform.killComponent()
                .get();
        System.out.println("killTime: " + (System.currentTimeMillis() - killStart));

        Map<String, Long> countMap = new HashMap<>();
        for(IExternalAccess agent: agents) {
            Map<String, Object> results = agent.getResultsAsync().get();
            RetData threadNames = (RetData) results.get("threadNames");
            //List<String> threadNames = (List<String>) results.get("threadNames");
            for(String name: threadNames) {
                long current = countMap.computeIfAbsent(name, _name -> 0L);
                countMap.put(name, current + 1L);
            }
        }

        System.out.println("agents: " + agents.size());
        long totalCalls = countMap.values()
                .stream()
                .mapToLong(value -> value)
                .sum();
        System.out.println("threadNames: " + countMap.size() + " (" + totalCalls + ")");
        countMap.forEach((name, count) -> System.out.println(name + ": " + count));
    }
}

/*
batch start
[00:01:13.247436300] [main] ...agents started: 57593
[00:01:13.247436300] [name#0] TEST
[00:01:18.254748400] [main] kill
killTime: 80898
agents: 20000
threadNames: 4 (200000)
Thread[Thread-8,5,main]:8850614, task=java.util.concurrent.ThreadPoolExecutor$Worker@568aec97[State = 1, empty queue]: 64370
Thread[Thread-37,5,main]:1721603470, task=java.util.concurrent.ThreadPoolExecutor$Worker@24881b57[State = 1, empty queue]: 14795
Thread[Thread-9,5,main]:389317966, task=java.util.concurrent.ThreadPoolExecutor$Worker@525f07b1[State = 1, empty queue]: 51916
Thread[Thread-22,5,main]:1921255999, task=java.util.concurrent.ThreadPoolExecutor$Worker@4723c98b[State = 1, empty queue]: 68919

single start
[00:23:16.506619700] [main] ...agents started: 68291
[00:23:16.522243700] [name#0] TEST
[00:23:21.530608300] [main] kill
killTime: 84909
agents: 20000
threadNames: 5 (200000)
Thread[Thread-12,5,main]:315864801, task=java.util.concurrent.ThreadPoolExecutor$Worker@4e7ab38b[State = 1, empty queue]: 49596
Thread[Thread-10,5,main]:1618829554, task=java.util.concurrent.ThreadPoolExecutor$Worker@40388e7c[State = 1, empty queue]: 1772
Thread[Thread-2,5,main]:154622404, task=java.util.concurrent.ThreadPoolExecutor$Worker@1bb10b55[State = 1, empty queue]: 51404
Thread[Thread-11,5,main]:27592901, task=java.util.concurrent.ThreadPoolExecutor$Worker@69742dc5[State = 1, empty queue]: 47505
Thread[Thread-9,5,main]:191850287, task=java.util.concurrent.ThreadPoolExecutor$Worker@7859df4c[State = 1, empty queue]: 49723


[00:46:02.334167600] [main] ...agents started: 494389
[00:46:02.352241200] [name#0] START
[00:46:07.343337700] [main] kill
killTime: 1172444
agents: 60000
threadNames: 98 (387799)
Thread[Thread-145,5,main]:2001152127, task=java.util.concurrent.ThreadPoolExecutor$Worker@3df1149e[State = 1, empty queue]: 277
Thread[Thread-158,5,main]:1645236725, task=java.util.concurrent.ThreadPoolExecutor$Worker@24ca1883[State = 1, empty queue]: 1
Thread[Thread-159,5,main]:791299630, task=java.util.concurrent.ThreadPoolExecutor$Worker@7b27366[State = 1, empty queue]: 2
Thread[Thread-114,5,main]:904181410, task=java.util.concurrent.ThreadPoolExecutor$Worker@713d3488[State = 1, empty queue]: 1
Thread[Thread-156,5,main]:1713173935, task=java.util.concurrent.ThreadPoolExecutor$Worker@4dc9c97f[State = 1, empty queue]: 1602
Thread[Thread-89,5,main]:1542904296, task=java.util.concurrent.ThreadPoolExecutor$Worker@195572cf[State = 1, empty queue]: 2506
Thread[Thread-142,5,main]:1865716552, task=java.util.concurrent.ThreadPoolExecutor$Worker@6b256d4d[State = 1, empty queue]: 4873
Thread[Thread-47,5,main]:487698547, task=java.util.concurrent.ThreadPoolExecutor$Worker@6bf26975[State = 1, empty queue]: 88
Thread[Thread-49,5,main]:1935050864, task=java.util.concurrent.ThreadPoolExecutor$Worker@6537b238[State = 1, empty queue]: 14018
Thread[Thread-107,5,main]:399695658, task=java.util.concurrent.ThreadPoolExecutor$Worker@7e276d90[State = 1, empty queue]: 1
Thread[Thread-51,5,main]:189010662, task=java.util.concurrent.ThreadPoolExecutor$Worker@1e50fdbb[State = 1, empty queue]: 12468
Thread[Thread-160,5,main]:964811939, task=java.util.concurrent.ThreadPoolExecutor$Worker@37563238[State = 1, empty queue]: 6
Thread[Thread-78,5,main]:2088635169, task=java.util.concurrent.ThreadPoolExecutor$Worker@7aba3978[State = 1, empty queue]: 91
Thread[Thread-152,5,main]:64201013, task=java.util.concurrent.ThreadPoolExecutor$Worker@6c79e458[State = 1, empty queue]: 1575
Thread[Thread-140,5,main]:1308817077, task=java.util.concurrent.ThreadPoolExecutor$Worker@1671ccc2[State = 1, empty queue]: 7790
Thread[Thread-157,5,main]:901621193, task=java.util.concurrent.ThreadPoolExecutor$Worker@5c418b4e[State = 1, empty queue]: 1370
Thread[Thread-146,5,main]:440815105, task=java.util.concurrent.ThreadPoolExecutor$Worker@70754dfc[State = 1, empty queue]: 3713
Thread[Thread-29,5,main]:2091323815, task=java.util.concurrent.ThreadPoolExecutor$Worker@f59678f[State = 1, empty queue]: 4296
Thread[Thread-88,5,main]:2072850489, task=java.util.concurrent.ThreadPoolExecutor$Worker@40915ca9[State = 1, empty queue]: 6660
Thread[Thread-8,5,main]:1915868915, task=java.util.concurrent.ThreadPoolExecutor$Worker@73cd0cef[State = 1, empty queue]: 31745
Thread[Thread-39,5,main]:2024307878, task=java.util.concurrent.ThreadPoolExecutor$Worker@7213dc0d[State = 1, empty queue]: 1731
Thread[Thread-42,5,main]:568378148, task=java.util.concurrent.ThreadPoolExecutor$Worker@72531548[State = 1, empty queue]: 10145
Thread[Thread-73,5,main]:255550619, task=java.util.concurrent.ThreadPoolExecutor$Worker@6b5ecb68[State = 1, empty queue]: 228
Thread[Thread-79,5,main]:505969036, task=java.util.concurrent.ThreadPoolExecutor$Worker@62f473e7[State = 1, empty queue]: 16
Thread[Thread-60,5,main]:102740621, task=java.util.concurrent.ThreadPoolExecutor$Worker@415b089c[State = 1, empty queue]: 33
Thread[Thread-54,5,main]:532852674, task=java.util.concurrent.ThreadPoolExecutor$Worker@273320cf[State = 1, empty queue]: 1629
Thread[Thread-36,5,main]:731969631, task=java.util.concurrent.ThreadPoolExecutor$Worker@34b2fcf[State = 1, empty queue]: 31633
Thread[Thread-148,5,main]:1600597880, task=java.util.concurrent.ThreadPoolExecutor$Worker@638e8b39[State = 1, empty queue]: 1681
Thread[Thread-147,5,main]:20721866, task=java.util.concurrent.ThreadPoolExecutor$Worker@30fa09ba[State = 1, empty queue]: 3118
Thread[Thread-64,5,main]:1999514942, task=java.util.concurrent.ThreadPoolExecutor$Worker@6e456376[State = 1, empty queue]: 24
Thread[Thread-93,5,main]:1109919950, task=java.util.concurrent.ThreadPoolExecutor$Worker@292b8a50[State = 1, empty queue]: 7029
Thread[Thread-75,5,main]:629735531, task=java.util.concurrent.ThreadPoolExecutor$Worker@7e0cdb4b[State = 1, empty queue]: 4
Thread[Thread-77,5,main]:598085608, task=java.util.concurrent.ThreadPoolExecutor$Worker@67a2ab6f[State = 1, empty queue]: 175
Thread[Thread-65,5,main]:307374167, task=java.util.concurrent.ThreadPoolExecutor$Worker@23c9802a[State = 1, empty queue]: 6
Thread[Thread-41,5,main]:1401933290, task=java.util.concurrent.ThreadPoolExecutor$Worker@4650b6c[State = 1, empty queue]: 651
Thread[Thread-104,5,main]:1487949005, task=java.util.concurrent.ThreadPoolExecutor$Worker@3a896e4d[State = 1, empty queue]: 1013
Thread[Thread-143,5,main]:578263839, task=java.util.concurrent.ThreadPoolExecutor$Worker@257ecb2f[State = 1, empty queue]: 2961
Thread[Thread-128,5,main]:516474605, task=java.util.concurrent.ThreadPoolExecutor$Worker@a902ea1[State = 1, empty queue]: 2
Thread[Thread-61,5,main]:1541909864, task=java.util.concurrent.ThreadPoolExecutor$Worker@bf0e45d[State = 1, empty queue]: 1253
Thread[Thread-57,5,main]:1620955461, task=java.util.concurrent.ThreadPoolExecutor$Worker@37026bde[State = 1, empty queue]: 1784
Thread[Thread-123,5,main]:1735679484, task=java.util.concurrent.ThreadPoolExecutor$Worker@781d2bab[State = 1, empty queue]: 1
Thread[Thread-68,5,main]:1154866967, task=java.util.concurrent.ThreadPoolExecutor$Worker@1f4d555a[State = 1, empty queue]: 409
Thread[Thread-53,5,main]:542433346, task=java.util.concurrent.ThreadPoolExecutor$Worker@fe33462[State = 1, empty queue]: 3623
Thread[Thread-63,5,main]:2005081191, task=java.util.concurrent.ThreadPoolExecutor$Worker@2f082266[State = 1, empty queue]: 1
Thread[Thread-2,5,main]:270929002, task=java.util.concurrent.ThreadPoolExecutor$Worker@2da5ce34[State = 1, empty queue]: 959
Thread[Thread-101,5,main]:943370082, task=java.util.concurrent.ThreadPoolExecutor$Worker@1c75d59b[State = 1, empty queue]: 1452
Thread[Thread-139,5,main]:1131759680, task=java.util.concurrent.ThreadPoolExecutor$Worker@4960eea8[State = 1, empty queue]: 3914
Thread[Thread-81,5,main]:1180335520, task=java.util.concurrent.ThreadPoolExecutor$Worker@10867b77[State = 1, empty queue]: 193
Thread[Thread-66,5,main]:1986037281, task=java.util.concurrent.ThreadPoolExecutor$Worker@61011548[State = 1, empty queue]: 189
Thread[Thread-7,5,main]:1659546188, task=java.util.concurrent.ThreadPoolExecutor$Worker@aaada29[State = 1, empty queue]: 1725
Thread[Thread-155,5,main]:1594025289, task=java.util.concurrent.ThreadPoolExecutor$Worker@605a1b84[State = 1, empty queue]: 1682
Thread[Thread-90,5,main]:5960574, task=java.util.concurrent.ThreadPoolExecutor$Worker@711a24f3[State = 1, empty queue]: 1
Thread[Thread-62,5,main]:1796176536, task=java.util.concurrent.ThreadPoolExecutor$Worker@7bb55ce2[State = 1, empty queue]: 977
Thread[Thread-71,5,main]:773255940, task=java.util.concurrent.ThreadPoolExecutor$Worker@41624684[State = 1, empty queue]: 32
Thread[Thread-56,5,main]:827969881, task=java.util.concurrent.ThreadPoolExecutor$Worker@21541bb2[State = 1, empty queue]: 8
Thread[Thread-95,5,main]:1205931338, task=java.util.concurrent.ThreadPoolExecutor$Worker@79f1eab1[State = 1, empty queue]: 1
Thread[Thread-137,5,main]:1680868187, task=java.util.concurrent.ThreadPoolExecutor$Worker@59eddd19[State = 1, empty queue]: 71
Thread[Thread-135,5,main]:58419249, task=java.util.concurrent.ThreadPoolExecutor$Worker@bc6165[State = 1, empty queue]: 7441
Thread[Thread-46,5,main]:1386524504, task=java.util.concurrent.ThreadPoolExecutor$Worker@4e019a2a[State = 1, empty queue]: 43
Thread[Thread-67,5,main]:1822032444, task=java.util.concurrent.ThreadPoolExecutor$Worker@2f975a02[State = 1, empty queue]: 256
Thread[Thread-120,5,main]:171155317, task=java.util.concurrent.ThreadPoolExecutor$Worker@1e20b53d[State = 1, empty queue]: 3492
Thread[Thread-161,5,main]:1962521548, task=java.util.concurrent.ThreadPoolExecutor$Worker@671dd467[State = 1, empty queue]: 235
Thread[Thread-144,5,main]:210364199, task=java.util.concurrent.ThreadPoolExecutor$Worker@7abae090[State = 1, empty queue]: 2948
Thread[Thread-76,5,main]:124833647, task=java.util.concurrent.ThreadPoolExecutor$Worker@2d09f468[State = 1, empty queue]: 7377
Thread[Thread-154,5,main]:289192644, task=java.util.concurrent.ThreadPoolExecutor$Worker@64fed981[State = 1, empty queue]: 2465
Thread[Thread-92,5,main]:545048462, task=java.util.concurrent.ThreadPoolExecutor$Worker@1b5a50fe[State = 1, empty queue]: 811
Thread[Thread-69,5,main]:183433137, task=java.util.concurrent.ThreadPoolExecutor$Worker@64bb1c7[State = 1, empty queue]: 148
Thread[Thread-138,5,main]:1399783966, task=java.util.concurrent.ThreadPoolExecutor$Worker@519d2666[State = 1, empty queue]: 6509
Thread[Thread-58,5,main]:1229127198, task=java.util.concurrent.ThreadPoolExecutor$Worker@98910a9[State = 1, empty queue]: 8
Thread[Thread-74,5,main]:632608324, task=java.util.concurrent.ThreadPoolExecutor$Worker@3e8a4b44[State = 1, empty queue]: 339
Thread[Thread-72,5,main]:107239357, task=java.util.concurrent.ThreadPoolExecutor$Worker@4f9c30d[State = 1, empty queue]: 277
Thread[Thread-118,5,main]:1299337265, task=java.util.concurrent.ThreadPoolExecutor$Worker@1e38c27[State = 1, empty queue]: 2
Thread[Thread-153,5,main]:1703988804, task=java.util.concurrent.ThreadPoolExecutor$Worker@7b396dad[State = 1, empty queue]: 1
Thread[Thread-48,5,main]:1667210236, task=java.util.concurrent.ThreadPoolExecutor$Worker@b7349c4[State = 1, empty queue]: 13301
Thread[Thread-150,5,main]:105046096, task=java.util.concurrent.ThreadPoolExecutor$Worker@67f75ddd[State = 1, empty queue]: 101
Thread[Thread-59,5,main]:1603777363, task=java.util.concurrent.ThreadPoolExecutor$Worker@a78e72b[State = 1, empty queue]: 13
Thread[Thread-32,5,main]:2126869979, task=java.util.concurrent.ThreadPoolExecutor$Worker@9e73f51[State = 1, empty queue]: 17095
Thread[Thread-149,5,main]:1042763683, task=java.util.concurrent.ThreadPoolExecutor$Worker@566e2632[State = 1, empty queue]: 2972
Thread[Thread-33,5,main]:1092643604, task=java.util.concurrent.ThreadPoolExecutor$Worker@61482873[State = 1, empty queue]: 26
Thread[Thread-134,5,main]:1886955854, task=java.util.concurrent.ThreadPoolExecutor$Worker@1ad3de64[State = 1, empty queue]: 2
Thread[Thread-45,5,main]:1389026689, task=java.util.concurrent.ThreadPoolExecutor$Worker@13b1ec99[State = 1, empty queue]: 25
Thread[Thread-70,5,main]:1682699132, task=java.util.concurrent.ThreadPoolExecutor$Worker@57c8bfa0[State = 1, empty queue]: 1727
Thread[Thread-136,5,main]:1980914510, task=java.util.concurrent.ThreadPoolExecutor$Worker@72b75bce[State = 1, empty queue]: 30
Thread[Thread-141,5,main]:413003331, task=java.util.concurrent.ThreadPoolExecutor$Worker@53b3cf4b[State = 1, empty queue]: 7919
Thread[Thread-80,5,main]:2122169151, task=java.util.concurrent.ThreadPoolExecutor$Worker@4b983e4a[State = 1, empty queue]: 34
Thread[Thread-52,5,main]:1370182085, task=java.util.concurrent.ThreadPoolExecutor$Worker@72fdb42b[State = 1, empty queue]: 5090
Thread[Thread-97,5,main]:1722608969, task=java.util.concurrent.ThreadPoolExecutor$Worker@4569ed87[State = 1, empty queue]: 5029
Thread[Thread-100,5,main]:1080281844, task=java.util.concurrent.ThreadPoolExecutor$Worker@16fa0716[State = 1, empty queue]: 1350
Thread[Thread-20,5,main]:500536501, task=java.util.concurrent.ThreadPoolExecutor$Worker@77bf2c22[State = 1, empty queue]: 12077
Thread[Thread-151,5,main]:958471486, task=java.util.concurrent.ThreadPoolExecutor$Worker@cae9bae[State = 1, empty queue]: 2501
Thread[Thread-50,5,main]:1750063898, task=java.util.concurrent.ThreadPoolExecutor$Worker@78c274fe[State = 1, empty queue]: 28
Thread[Thread-13,5,main]:1118854386, task=java.util.concurrent.ThreadPoolExecutor$Worker@ee72f9d[State = 1, empty queue]: 9974
Thread[Thread-4,5,main]:1376476643, task=java.util.concurrent.ThreadPoolExecutor$Worker@3a35d990[State = 1, empty queue]: 75
Thread[Thread-43,5,main]:648933879, task=java.util.concurrent.ThreadPoolExecutor$Worker@642fad78[State = 1, empty queue]: 23666
Thread[Thread-55,5,main]:900256922, task=java.util.concurrent.ThreadPoolExecutor$Worker@3e17bcb7[State = 1, empty queue]: 51130
Thread[Thread-96,5,main]:716440950, task=java.util.concurrent.ThreadPoolExecutor$Worker@306588d1[State = 1, empty queue]: 9493
Thread[Thread-127,5,main]:1452622431, task=java.util.concurrent.ThreadPoolExecutor$Worker@16851519[State = 1, empty queue]: 1
Thread[Thread-37,5,main]:1786432470, task=java.util.concurrent.ThreadPoolExecutor$Worker@7edba0de[State = 1, empty queue]: 18351
 */