package com.vt.flow.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

/**
 * 嵌套结构递归查询工具，可设置在递归中对满足条件的对象进行的操作，当前同一个{@link Searcher Searcher}不支持多线程操作<br/>
 * 针对起始数据为 单个对象/单个集合/单个map<br/>
 * 对单一或多个属性进行递归查询，要求每层的对象结构一致<br/>
 * 可对多个属性取值判断，通过AND和OR确定整体逻辑关系，不会对判断对象进行空校验，在设置判断行为时，需根据实际情况对控制进行处理<br/>
 * judgeLogic，filter相互配合，可满足4中条件组合模式：<br/>
 *  1. a && b && c ...<br/>
 *  2. a || b || c ...<br/>
 *  3. (a && b && c ...) && filter<br/>
 *  4. (a || b || c ...) && filter<br/>
 */
public class RecursionSearchUtils {

    private RecursionSearchUtils(){}

    /**
     * 从对象递归，获取一个满足的结果，多个条件AND逻辑
     * @param v 起始对象
     * @return V
     * @param <V> 对象类型
     */
    public static <V> Searcher<V> startObjGetOneAnd(V v) {
        return new Searcher<V>(v, judgeLogicAnd(), (s) -> s.recursionStartObj(v));
    }

    /**
     * 从对象递归，获取一个满足的结果，多个条件OR逻辑
     * @param v 起始对象
     * @return V
     * @param <V> 对象类型
     */
    public static <V> Searcher<V> startObjGetOneOR(V v) {
        return new Searcher<V>(v, judgeLogicOR(), (s) -> s.recursionStartObj(v));
    }

    /**
     * 从集合元素递归，获取一个满足的结果，多个条件AND逻辑
     * @param collection 起始集合
     * @return V
     * @param <V> 集合元素类型
     */
    public static <V> Searcher<V> startCollectionGetOneAnd(Collection<V> collection) {
        return new Searcher<V>(collection, judgeLogicAnd(), (s) -> s.recursionStartCollection(collection));
    }

    /**
     * 从集合元素递归，获取一个满足的结果，多个条件OR逻辑
     * @param collection 起始集合
     * @return V
     * @param <V> 集合元素类型
     */
    public static <V> Searcher<V> startCollectionGetOneOR(Collection<V> collection) {
        return new Searcher<V>(collection, judgeLogicOR(), (s) -> s.recursionStartCollection(collection));
    }

    /**
     * 从map values递归，获取一个满足的结果，多个条件AND逻辑
     * @param map 起始map
     * @return V
     * @param <V> map value类型
     */
    public static <V> Searcher<V> startMapGetOneAnd(Map<?,V> map) {
        return new Searcher<V>(map, judgeLogicAnd(), (s) -> s.recursionStartMap(map));
    }

    /**
     * 从map values递归，获取一个满足的结果，多个条件OR逻辑
     * @param map 起始map
     * @return V
     * @param <V> map value类型
     */
    public static <V> Searcher<V> startMapGetOneOR(Map<?,V> map) {
        return new Searcher<V>(map, judgeLogicOR(), (s) -> s.recursionStartMap(map));
    }

    /**
     * 从对象递归，获取所有满足的结果，多个条件AND逻辑
     * @param v 起始对象
     * @return V
     * @param <V> 对象类型
     */
    public static <V> Searcher<V> startObjGetAllAnd(V v) {
        return new Searcher<V>(v, judgeLogicAnd(), (s) -> s.recursionStartObjAll(v));
    }

    /**
     * 从对象递归，获取所有满足的结果，多个条件OR逻辑
     * @param v 起始对象
     * @return V
     * @param <V> 对象类型
     */
    public static <V> Searcher<V> startObjGetAllOR(V v) {
        return new Searcher<V>(v, judgeLogicOR(), (s) -> s.recursionStartObjAll(v));
    }

    /**
     * 从集合元素递归，获取所有满足的结果，多个条件AND逻辑
     * @param collection 起始集合
     * @return V
     * @param <V> 集合元素类型
     */
    public static <V> Searcher<V> startCollectionGetAllAnd(Collection<V> collection) {
        return new Searcher<V>(collection, judgeLogicAnd(), (s) -> s.recursionStartCollectionAll(collection));
    }

    /**
     * 从集合元素递归，获取所有满足的结果，多个条件OR逻辑
     * @param collection 起始集合
     * @return V
     * @param <V> 集合元素类型
     */
    public static <V> Searcher<V> startCollectionGetAllOR(Collection<V> collection) {
        return new Searcher<V>(collection,  judgeLogicOR(), (s) -> s.recursionStartCollectionAll(collection));
    }

    /**
     * 从map values递归，获取所有满足的结果，多个条件AND逻辑
     * @param map 起始map
     * @return V
     * @param <V> map value类型
     */
    public static <V> Searcher<V> startMapGetAllAnd(Map<?,V> map) {
        return new Searcher<V>(map, judgeLogicAnd(), (s) -> s.recursionStartMapAll(map));
    }

    /**
     * 从map values递归，获取所有满足的结果，多个条件OR逻辑
     * @param map 起始map
     * @return V
     * @param <V> map value类型
     */
    public static <V> Searcher<V> startMapGetAllOR(Map<?,V> map) {
        return new Searcher<V>(map, judgeLogicOR(), (s) -> s.recursionStartMapAll(map));
    }

    /**
     * AND逻辑
     * 不会对判断对象进行空校验，在设置判断行为时，需根据实际情况对控制进行处理
     * @return BiPredicate
     * @param <V> V
     */
    private static <V> BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogicAnd() {
        return  (j,v) -> {
            for (Function<V, ?> judge : j.keySet()) {
                Object apply = judge.apply(v);
                if (!j.get(judge).test(apply)) return false;
            }
            return true;
        };
    }

    /**
     * OR逻辑
     * 不会对判断对象进行空校验，在设置判断行为时，需根据实际情况对控制进行处理
     * @return BiPredicate
     * @param <V> V
     */
    private static <V> BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogicOR() {
        return (j,v) -> {
            for (Function<V, ?> judge : j.keySet()) {
                Object apply = judge.apply(v);
                if (j.get(judge).test(apply)) return true;
            }
            return false;
        };
    }

    /**
     * 查询实际执行对象
     */
    public static class Searcher<V> {

        private Searcher() {}

        private Class<?> classFlag;

        private Consumer<Searcher<V>> operation;

        /**
         * 需要递归的属性
         */
        private final List<Function<V,?>> recursionParam = new ArrayList<>();

        /**
         * 判断值，可比较多个属性的值
         */
        private final Map<Function<V,?>, Predicate<Object>> judgeValue = new HashMap<>();

        /**
         * 判断逻辑 AND/OR
         */
        private BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogic;

        /**
         * 得到结果后的处理
         */
        private Consumer<V> postProcess;

        /**
         * 是否过滤循环引用，默认false
         * true - 跳过被处理过的对象
         * false - 不跳过被处理过的对象，循环引用将引起无限递归
         */
        private boolean cycleFilter = false;

        /**
         * 循环引用判断
         */
        private Predicate<Integer> cyclePredicate;

        /**
         * 循环引用记录，存储对象的hashcode
         * 采用全局设置的配置，若因为复杂情景需要将递归拆解整多次方法调用，便于继承之前阶段的循环引用记录
         */
        private Set<Integer> cycle;

        /**
         * 用于获取一个满足的结果时，提前终止递归
         */
        private boolean stopFlag;

        private Searcher (V obj,
                          BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogic,
                          Consumer<Searcher<V>> operation) {
            if (obj == null) throw new NullPointerException("起始对象为null");
            this.classFlag = obj.getClass();
            this.operation = operation;
            this.judgeLogic = judgeLogic;
        }

        private Searcher(Collection<V> collection,
                         BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogic,
                         Consumer<Searcher<V>> operation) {
            if (collection == null || collection.isEmpty()) throw new NullPointerException("起始集合为null");
            this.classFlag = collection.iterator().next().getClass();
            this.operation = operation;
            this.judgeLogic = judgeLogic;
        }

        private Searcher(Map<?,V> map,
                         BiPredicate<Map<Function<V,?>, Predicate<Object>>, V> judgeLogic,
                         Consumer<Searcher<V>> operation) {
            if (map == null || map.isEmpty()) throw new NullPointerException("起始map为null");
            this.classFlag = map.values().iterator().next().getClass();
            this.operation = operation;
            this.judgeLogic = judgeLogic;
        }

        /**
         * 设置递归属性
         * @param recursionParam 对象需要递归的属性
         */
        public Searcher<V> addRecursionParam(Function<V, ?> recursionParam) {
            if (recursionParam != null) this.recursionParam.add(recursionParam);
            return this;
        }

        /**
         * 设置判断置
         * @param function 从对象获取属性值的行为
         * @param predicate 判断要满足的值的行为
         */
        public Searcher<V> putJudgeValue(Function<V,?> function, Predicate<Object> predicate) {
            if (predicate != null && function != null) judgeValue.put(function, predicate);
            return this;
        }

        /**
         * 设置过滤循环引用
         */
        public Searcher<V> cycleFilter() {
            return cycleFilter(true);
        }

        /**
         * 设置不过滤循环引用
         */
        public Searcher<V> unCycleFilter() {
            return cycleFilter(false);
        }

        /**
         * 设置是否过滤循环引用
         */
        public Searcher<V> cycleFilter(boolean filter) {
            cycleFilter = filter;
            return this;
        }

        /**
         * 清空循环引用记录
         * 如果需要复用Search，又不需要继承之前的循环引用记录，可清空 cycle
         */
        public Searcher<V> clearCycleRecord() {
            if (cycle != null) cycle.clear();
            return this;
        }

        /**
         * 若因为复杂情景需要将递归拆解整多次方法调用，便于继承之前阶段的循环引用记录
         * @param cycle 循环引用记录
         */
        public Searcher<V> setCycle(Set<Integer> cycle) {
            if (cycle != null && !cycle.isEmpty()) {
                if (this.cycle == null) this.cycle = new HashSet<>(cycle.size());
                this.cycle.addAll(cycle);
            }
            return this;
        }

        /**
         * 获取循环引用记录
         * @return Set<Integer>
         */
        public Set<Integer> getCycle() {
            return this.cycle;
        }

        private void check() {
            if (recursionParam.isEmpty() || judgeValue.isEmpty()) throw new NullPointerException("需要递归的属性集合为空或者需要判断属性值的操作为空");
        }

        /**
         * 循环引用判断逻辑初始化
         */
        private void cyclePredicateInit() {
            if (cycleFilter) {
                if (cycle == null) cycle = new HashSet<>();
                cyclePredicate = cycle::add;
            } else {
                cyclePredicate = v -> true;
            }
        }

        /**
         * 获取结果，配合one方法
         * @return V 第一个满足的结果
         */
        public V get() {
            Predicate<V> predicate = v -> true;
            return get(predicate);
        }

        /**
         * 获取结果，对结果再次过滤，配合one方法
         * @param filter 过滤条件
         * @return V 第一个满足的结果
         */
        public V get(@NotNull Predicate<V> filter) {
            return get(Function.identity(), filter);
        }

        /**
         * 获取结果，对结果进行后置处理，配合one方法
         * @param process 后置处理
         */
        public <M> M get(@NotNull Function<V, M> process) {
            return get(process, v -> true);
        }

        /**
         * 获取结果，对结果再次过滤并进行后置处理，配合one方法
         * @param process 处理方法
         * @param filter 过滤条件
         * @return M 处理后的结果
         */
        public <M> M get(@NotNull Function<V, M> process, @NotNull Predicate<V> filter) {
            check();
            stopFlag = false;
            cyclePredicateInit();
            AtomicReference<M> reference = new AtomicReference<>(null);
            this.postProcess = (v) -> {
                if (filter.test(v)) {
                    reference.set(process.apply(v));
                    stopFlag = true;
                }
            };
            operation.accept(this);
            return reference.get();
        }

        /**
         * 获取结果列表，配合all方法
         * @return List<V> 处理后的结果
         */
        public List<V> getAll() {
            Predicate<V> predicate = v -> true;
            return getAll(predicate);
        }

        /**
         * 获取结果列表，并对结果再次过滤，配合all方法
         * @param filter 过滤条件
         * @return List<V> 处理后的结果
         */
        public List<V> getAll(@NotNull Predicate<V> filter) {
            return getAll(new ArrayList<V>(), (v, m) -> m.add(v), filter);
        }

        /**
         * 获取结果列表，对结果进行后置处理，配合all方法
         * @param process 后置处理
         * @return List<M> 处理后的结果
         */
        public <M> List<M> getAll(@NotNull Function<V, M> process) {
            return getAll(process, v -> true);
        }

        /**
         * 获取结果列表，并对结果再次过滤并进行后置处理，配合all方法
         * @return List<M> 处理后的结果
         */
        public <M> List<M> getAll(@NotNull Function<V, M> process, @NotNull Predicate<V> filter) {
            return getAll(new ArrayList<M>(), (v, m) -> m.add(process.apply(v)), filter);
        }

        /**
         * 获取所有结果，对结果再次过滤并进行后置处理，配合all方法
         * @param result 接收结果
         * @param process 处理方法
         * @param filter 进行后置操作前的过滤条件
         * @return M 处理后的结果
         */
        public <M> M getAll(@NotNull M result, @NotNull BiConsumer<V, M> process, @NotNull Predicate<V> filter) {
            check();
            cyclePredicateInit();
            this.postProcess = (v) -> {
                if (filter.test(v)) process.accept(v, result);
            };
            operation.accept(this);
            return result;
        }

        /**
         * 分配方法，配合one方法
         * @param obj obj
         */
        @SuppressWarnings("unchecked")
        private void assignMethod(Object obj) {
            if (obj != null) {
                if (classFlag.equals(obj.getClass())) recursionStartObj((V) obj);
                else if (obj instanceof Collection<?> c && !c.isEmpty() && classFlag.equals(c.iterator().next().getClass())) recursionStartCollection((Collection<V>) c);
                else if (obj instanceof Map<?,?> m && !m.isEmpty() && classFlag.equals(m.values().iterator().next())) recursionStartMap((Map<?, V>) m);
            }
        }

        /**
         * 分配方法，配合all方法
         * @param obj obj
         */
        @SuppressWarnings("unchecked")
        private void assignMethodAll(Object obj) {
            if (obj != null) {
                if (classFlag.equals(obj.getClass())) recursionStartObjAll((V) obj);
                else if (obj instanceof Collection<?> c && !c.isEmpty() && classFlag.equals(c.iterator().next().getClass())) recursionStartCollectionAll((Collection<V>) c);
                else if (obj instanceof Map<?,?> m && !m.isEmpty() && classFlag.equals(m.values().iterator().next())) recursionStartMapAll((Map<?, V>) m);
            }
        }

        /**
         * 从单个对象递归获取结果
         */
        private void recursionStartObj(V v) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(v))) return;

            //比较第一层的值
            if (judgeLogic.test(judgeValue, v)) {
                this.postProcess.accept(v);
                if (stopFlag) return;
            }

            //第一层不满足，获取递归属性，进行递归
            for (Function<V, ?> getParam : recursionParam) {
                if (stopFlag) break;
                Object apply = getParam.apply(v);
                assignMethod(apply);
            }
        }

        /**
         * 对集合中的对象递归获取结果
         * @param collection collection
         */
        private void recursionStartCollection(Collection<V> collection) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(collection))) return;
            for (V v : collection) recursionStartObj(v);
        }

        /**
         * 对map中的value递归获取结果
         * @param map map
         */
        private void recursionStartMap(Map<?,V> map) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(map))) return;
            for (Object key : map.keySet()) {
                Object entry = map.get(key);
                assignMethod(entry);
            }
        }

        /**
         * 从单个对象递归获取结果集合
         */
        private void recursionStartObjAll(V v) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(v))) return;

            //比较第一层的值
            if (judgeLogic.test(judgeValue, v))  this.postProcess.accept(v);

            //第一层不满足，获取递归属性，进行递归
            for (Function<V, ?> getParam : recursionParam) {
                Object apply = getParam.apply(v);
                assignMethodAll(apply);
            }
        }

        /**
         * 对集合中的对象递归获取结果集合
         * @param collection collection
         */
        private void recursionStartCollectionAll(Collection<V> collection) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(collection))) return;
            for (V v : collection) recursionStartObjAll(v);
        }

        /**
         * 对map中的value递归获取结果集合
         * @param map map
         */
        private void recursionStartMapAll(Map<?,V> map) {
            //未通过循环引用判断
            if (!cyclePredicate.test(System.identityHashCode(map))) return;
            for (Object key : map.keySet()) {
                Object entry = map.get(key);
                assignMethodAll(entry);
            }
        }
    }

}
