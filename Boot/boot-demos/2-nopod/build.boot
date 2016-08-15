(set-env!
 :source-paths #{"task"}
 :dependencies '[[alandipert/upcase "2.0.0"]])

(require '[alandipert.upcase :refer [upcase!]])

(defn- lc-files [fileset]
  (->> fileset
       input-files
       (by-ext [".lc"])))

(defn lc->uc-path [path]
  (clojure.string/replace path #".lc$" ".uc"))

(defn lc->uc [dir files]
  (doseq [f files
          :let [in-file (tmp-file f)
                rel-path (tmp-path f)]]
    (info "Upcasing %s...\n" rel-path)
    (upcase! in-file dir (lc->uc-path rel-path))))

(deftask upper
  []
  (let [tmp (tmp-dir!)]
    (with-pre-wrap fileset
      (empty-dir! tmp)
      (lc->uc tmp (lc-files fileset))
      (commit! (add-resource fileset tmp)))))
