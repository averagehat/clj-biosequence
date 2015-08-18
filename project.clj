(defproject clj-biosequence "0.3.9"
  :description "Library for the manipulation of biological sequences."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["biojava" "http://www.biojava.org/download/maven/"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.apache.commons/commons-compress "1.9"]
                 [com.taoensso/nippy "2.9.0"]
                 [com.velisco/tagged "0.3.7"]
                 [clj-http "2.0.0"]
                 [iota "1.1.2"]
                 [clj-time "0.11.0"]
                 [fs "1.3.3"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojars.hozumi/clj-commons-exec "1.2.0"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [postgresql "9.3-1102.jdbc41"]
                 [com.mchange/c3p0 "0.9.5"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.2"]]
  :resource-paths ["shared" "resources"]
  :plugins [[codox "0.8.10"]]
  :codox {:src-dir-uri "https://github.com/s312569/clj-biosequence/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :repl-options {:init (set! *print-length* 100)}
  :jvm-opts ["-Xmx1000M"])
