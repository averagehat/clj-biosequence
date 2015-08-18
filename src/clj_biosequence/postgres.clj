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
  (db/db-do-commands conn (db/create-table-ddl
                           tname
                           (doall
                            (map (fn [[k v]]
                                   (vector k (first v))) schema))))
  (apply db/db-do-commands conn (doall
                                 (map (fn [[k v]]
                                        (str "CREATE INDEX "
                                             (name k)
                                             "_ix ON "
                                             tname
                                             " (" (name k) ")"))
                                      (dissoc :record schema)))))

(defn- get-vals
  [s bs]
  (-> (map (fn [[_ v]]
             ((second v) bs)) s)
      vec))

(defn init-and-save
  ([conn tname col] (init-and-save conn tname col nil))
  ([conn tname col schema]
   (let [s (merge {:accession ["varchar(256)" #(bs/accession %)]
                   :record ["text" #(pr-str %)]}
                  schema)]
     (db/with-db-transaction [c @conn]
       (table-create-index c tname s)
       (apply db/insert! (keys schema) (map #(get-vals schema %) col))))))

(defn append-bioseqs
  [conn tname col])

(defn remove-bioseqs
  [conn tname accessions])

(defn delete-collection
  [conn tname])



