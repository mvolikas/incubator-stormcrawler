# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#!/bin/bash

collections=("docs" "metrics" "status")

for collection in "${collections[@]}"; do
    solr_url="http://localhost:8983/solr/$collection/update?commit=true"
    
    echo "Deleting all documents from collection: $collection ..."

    curl -X POST -H 'Content-Type: application/json' --data-binary '{"delete": {"query": "*:*"}}' "$solr_url"

    if [ $? -eq 0 ]; then
        echo "Successfully deleted all documents from core: $collection"
    else
        echo "Failed to delete documents from core: $collection"
    fi
done