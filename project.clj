(defproject highway-to-hell "0.1.0-SNAPSHOT"
  :main ^{:skip-aot true} highway-to-hell.song
  :jvm-opts ^:replace []
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [overtone "0.10.1"]
                 [shadertone "0.2.5"]
                 [leipzig "0.10.0"]])
