public class Main {
    public static void main(final String[] args) {
        final SkipList<Integer, String> list = new SkipList<>();
        for(int i = 0; i < 5; i++) {
            list.put(i, "ID: " + i);
        }
        System.out.println(list.get(2));
        list.remove(2);
        System.out.println(list);
    }
}
