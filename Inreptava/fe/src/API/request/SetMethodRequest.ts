import ClassModel from "../ClassModel";
import Field from "../Field";

interface SetMethodRequest {
    fullClassName: string,
    method: string,
    add: boolean
    field: Field
}
export default SetMethodRequest;