package agent;

enum DataExporter {
    CONSOLE("console"),
    SERVER("server");

    private final String name;

    DataExporter(String name) {
        this.name = name;
    }

    static DataExporter of(String name) {
        for (DataExporter dataExporter : DataExporter.values()) {
            if (name.equals(dataExporter.name)) {
                return dataExporter;
            }
        }

        return CONSOLE;
    }
}
