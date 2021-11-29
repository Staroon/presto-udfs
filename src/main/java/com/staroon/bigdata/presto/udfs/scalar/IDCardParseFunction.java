/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.staroon.bigdata.presto.udfs.scalar;

import cn.hutool.core.util.IdcardUtil;
import io.airlift.slice.Slice;
import io.prestosql.spi.PrestoException;
import io.prestosql.spi.function.Description;
import io.prestosql.spi.function.ScalarFunction;
import io.prestosql.spi.function.SqlNullable;
import io.prestosql.spi.function.SqlType;
import io.prestosql.spi.type.StandardTypes;

import static io.prestosql.spi.StandardErrorCode.INVALID_ARGUMENTS;

/**
 * @author Staroon Created on 2021/07/28 15:22
 * @version 1.0
 */
public class IDCardParseFunction
{
    private IDCardParseFunction() {}

    @ScalarFunction("idcard_parse")
    @Description("Parse the Chinese ID Card, parse age or sex(1:male, 0:female).")
    @SqlNullable
    @SqlType(StandardTypes.INTEGER)
    public static Long idToAge(@SqlType(StandardTypes.VARCHAR) Slice mode, @SqlType(StandardTypes.VARCHAR) Slice idSlice)
    {
        String id = idSlice.toStringUtf8();
        // 判断是否是合法的身份证号
        if (IdcardUtil.isValidCard(id)) {
            int res;
            switch (mode.toStringUtf8().toLowerCase()){
                case "age":
                    // 精确到 日 的年龄计算
                    try {
                        res = IdcardUtil.getAgeByIdCard(id);
                    }
                    catch (IllegalArgumentException e) {
                        // 捕获生日大于当前时间的异常
                        return null;
                    }
                    break;
                case "sex":
                    // 性别，1：男，0：女
                    res = IdcardUtil.getGenderByIdCard(id);
                    break;
                default:
                    throw new PrestoException(INVALID_ARGUMENTS, "Invalid arguments, the first param could be: age or sex.");
            }
            return (long) res;
        }
        else {
            return null;
        }
    }
}
