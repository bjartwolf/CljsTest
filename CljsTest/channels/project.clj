(defproject channels "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
				 [prismatic/dommy "0.1.1"]
				 [org.clojure/core.async "0.1.222.0-83d0c2-alpha"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "..\\js\\channels.js"
								   :optimizations :simple
                                   :pretty-print false}}]})