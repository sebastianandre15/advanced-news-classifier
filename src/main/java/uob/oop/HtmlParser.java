package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        String titleTagOpen = "<title>";
        String titleTagClose = "</title>";

        int titleStart = _htmlCode.indexOf(titleTagOpen) + titleTagOpen.length();
        int titleEnd = _htmlCode.indexOf(titleTagClose);

        if (titleStart != -1 && titleEnd != -1 && titleEnd > titleStart) {
            String strFullTitle = _htmlCode.substring(titleStart, titleEnd);
            return strFullTitle.substring(0, strFullTitle.indexOf(" |"));
        }

        return "Title not found!";
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        String contentTagOpen = "\"articleBody\": \"";
        String contentTagClose = " \",\"mainEntityOfPage\":";

        int contentStart = _htmlCode.indexOf(contentTagOpen) + contentTagOpen.length();
        int contentEnd = _htmlCode.indexOf(contentTagClose);

        if (contentStart != -1 && contentEnd != -1 && contentEnd > contentStart) {
            return _htmlCode.substring(contentStart, contentEnd).toLowerCase();
        }

        return "Content not found!";
    }

    public static NewsArticles.DataType getDataType(String _htmlCode) {
        //TODO Task 3.1 - 1.5 Marks
        String typeTagOpen = "<datatype>";
        String typeTagClose = "</datatype>";

        int dataTypeStart = _htmlCode.indexOf(typeTagOpen) + typeTagOpen.length();
        int dataTypeEnd = _htmlCode.indexOf(typeTagClose);

        if (_htmlCode.indexOf(typeTagOpen) != -1 && dataTypeEnd != -1 && dataTypeEnd > dataTypeStart) {
            return NewsArticles.DataType.valueOf(_htmlCode.substring(dataTypeStart, dataTypeEnd));
        } else {
            return NewsArticles.DataType.Testing; //Please modify the return value.
        }
    }

    public static String getLabel (String _htmlCode) {
        //TODO Task 3.2 - 1.5 Marks
        String labelTagOpen = "<label>";
        String labelTagClose = "</label>";

        int labelStart = _htmlCode.indexOf(labelTagOpen) + labelTagOpen.length();
        int labelEnd = _htmlCode.indexOf((labelTagClose));

        if (_htmlCode.indexOf(labelTagOpen) != -1 && labelEnd != -1 && labelEnd > labelStart) {
            return _htmlCode.substring(labelStart, labelEnd);
        } else {
            return "-1"; //Please modify the return value.
        }
    }


}
