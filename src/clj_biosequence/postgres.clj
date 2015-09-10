(ns clj-biosequence.postgres
  (:require [clojure.java.jdbc :as db]
            [clj-biosequence.core :as bs]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [jdbc.pool.c3p0 :as pool])
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(defn spec
  "Returns a delay object for accessing database connection pool."
  [dbname & {:keys [host port password user] :or {host "127.0.0.1"
                                                  port "5432"
                                                  password nil
                                                  user "postgres"}}]
  (pool/make-datasource-spec
   {:classname "org.postgresql.Driver"
    :subprotocol "postgresql"
    :user user
    :password password
    :subname (str "//" host ":" port "/" dbname)}))

(defn- table-create-index
  [conn tname schema]
  (db/db-do-commands conn
                     (apply db/create-table-ddl
                            tname
                            (doall
                             (map (fn [[k v]]
                                    (vector k (first v))) schema))))
  (apply db/db-do-commands conn (doall
                                 (map (fn [[k v]]
                                        (str "CREATE INDEX "
                                             (name k)
                                             tname
                                             "_ix ON "
                                             tname
                                             "(" (name k) ")"))
                                      (dissoc schema :record)))))

(defn- get-vals
  [s bs]
  (-> (map (fn [[_ v]]
             ((second v) bs)) s)
      vec))

(defrecord biosequenceDB [spec])

(defn- insert-bs
  [c tname k v]
  (apply db/insert! c tname (map #(zipmap k %) v)))

(defn init-and-save
  ([conn tname col] (init-and-save conn tname col nil))
  ([conn tname col schema]
   (let [s (merge {:accession ["varchar(256)" #(bs/accession %)]
                   :record ["text" #(pr-str %)]}
                  schema)]
     (db/with-db-transaction [c conn]
       (try
         (let [k (vec (keys s))
               v (map #(get-vals s %) col)]
           (table-create-index c tname s)
           (insert-bs c tname k v))
         (catch Exception e (.getNextException e)))))))

(defn append-bioseqs
  [conn tname col])

(defn remove-bioseqs
  [conn tname accessions])

(defn delete-collection
  [conn tname])



;; (jdbc/db-do-commands (spec "jellydb" :user "jason" :password "7004jason")
;;                            (jdbc/create-table-ddl
;;                             :ticks2
;;                             [:id :serial "PRIMARY KEY"]
;;                             [:body :varchar "NOT NULL"]
;;                             [:tick :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
