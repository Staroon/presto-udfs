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

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.prestosql.spi.PrestoException;
import io.prestosql.spi.function.Description;
import io.prestosql.spi.function.ScalarFunction;
import io.prestosql.spi.function.SqlNullable;
import io.prestosql.spi.function.SqlType;
import io.prestosql.spi.type.StandardTypes;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.io.FileNotFoundException;

import static io.prestosql.spi.StandardErrorCode.COMPILER_ERROR;
import static io.prestosql.spi.StandardErrorCode.INVALID_ARGUMENTS;

/**
 * @author Staroon Created on 2020/12/31 17:54
 * @version 1.0
 */
public class IPLocationFunction
{
    private static final DbSearcher SEARCHER;

    static {
        String filePath = new IPLocationFunction().getJarHome();
        String dbFile = filePath + "/ip2region.db";
        try {
            DbConfig config = new DbConfig();
            SEARCHER = new DbSearcher(config, dbFile);
        }
        catch (DbMakerConfigException e) {
            throw new PrestoException(COMPILER_ERROR, "ip2region config init failed.");
        }
        catch (FileNotFoundException e) {
            throw new PrestoException(COMPILER_ERROR, "ip2region db file not find.");
        }
    }

    private IPLocationFunction() {}

    @ScalarFunction("ip_location")
    @Description("Return the input ip location: country, province, city, isp")
    @SqlNullable
    @SqlType(StandardTypes.VARCHAR)
    public static Slice ipToRegion(@SqlType(StandardTypes.VARCHAR) Slice mode, @SqlType(StandardTypes.VARCHAR) Slice ipSlice)
    {
        String ip = ipSlice.toStringUtf8();
        String locations;

        if (Util.isIpAddress(ip)) {
            try {
                locations = SEARCHER.memorySearch(ip).getRegion();
            }
            catch (Exception e) {
                return null;
            }
        }
        else {
            return null;
        }

        String[] res = locations.split("\\|");
        if (res.length < 5) {
            return Slices.utf8Slice(locations);
        }
        String result;
        switch (mode.toStringUtf8().toLowerCase()) {
            case "country":
                result = res[0];
                break;
            case "province":
                result = res[2];
                break;
            case "city":
                result = res[3];
                break;
            case "isp":
                result = res[4];
                break;
            default:
                throw new PrestoException(INVALID_ARGUMENTS, "Invalid arguments, the first param could be: country, province, city, isp.");
        }
        return Slices.utf8Slice(result);
    }

    private String getJarHome()
    {
        return new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    }
}
