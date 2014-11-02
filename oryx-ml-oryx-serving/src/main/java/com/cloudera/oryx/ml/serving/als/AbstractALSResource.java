/*
 * Copyright (c) 2014, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.oryx.ml.serving.als;

import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import com.cloudera.oryx.common.collection.Pair;
import com.cloudera.oryx.ml.serving.AbstractOryxResource;
import com.cloudera.oryx.ml.serving.IDValue;
import com.cloudera.oryx.ml.serving.als.model.ALSServingModel;

public abstract class AbstractALSResource extends AbstractOryxResource {

  private ALSServingModel alsServingModel;

  @PostConstruct
  public void init() {
    super.init();
    alsServingModel = (ALSServingModel) getServingModelManager().getModel();
  }

  protected final ALSServingModel getALSServingModel() {
    return alsServingModel;
  }

  protected static <T> List<T> selectedSublist(List<T> values, int howMany, int offset) {
    if (values.size() < offset) {
      return Collections.emptyList();
    }
    return values.subList(offset, Math.min(offset + howMany, values.size()));
  }

  protected static List<IDValue> toIDValueResponse(List<Pair<String,Double>> pairs,
                                                   int howMany,
                                                   int offset) {
    List<Pair<String,Double>> sublist = selectedSublist(pairs, howMany, offset);
    return Lists.transform(sublist,
        new Function<Pair<String,Double>,IDValue>() {
          @Override
          public IDValue apply(Pair<String,Double> idDot) {
            return new IDValue(idDot.getFirst(), idDot.getSecond());
          }
        });
  }

}
