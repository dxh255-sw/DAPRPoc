package com.sw.dapr.daprpoc.beans;

import java.util.*;

public class Root {
    private List<VaultItem> results = new ArrayList<VaultItem>();

    public List<VaultItem> getResults() {
        return results;
    }

    public void setResults(List<VaultItem> results) {
        this.results = results;
    }
}

