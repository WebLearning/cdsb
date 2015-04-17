/**
 * Created by QK on 2014/12/4.
 */

function goCommentDetails(thisHtml)
{
    document.getElementById("comment").className="tab-pane";
    document.getElementById("commentDetails").className="tab-pane active";
    changeComment(thisHtml);
}

function goCommentList()
{
    document.getElementById("comment").className="tab-pane active";
    document.getElementById("commentDetails").className="tab-pane";
}

function changeComment(articleTitle)
{

}
