package cat.xtec.merli.client.zonaclic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import cat.xtec.merli.domain.type.LangString;


/**
 * JSON deserializer for language strings.
 *
 * This deserializer taskes a JSON object that contains localized strings
 * and converts them to a list of {@code LangString} objects. The expected
 * JSON object has the form <code>{ "key": "value, […] }</code> where «key»
 * is an ISO 639-1 language code and «value» is the string for that language.
 */
public class StringDeserializer extends JsonDeserializer<List<LangString>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LangString> deserialize(
        JsonParser parser, DeserializationContext context)
        throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        Iterator<String> tags = node.fieldNames();
        List<LangString> results = new ArrayList<>();

        while (tags.hasNext()) {
            String tag = tags.next();
            String text = node.get(tag).textValue();

            results.add(LangString.from(text, tag));
        }

        return results;
    }

}
