import Field from "./Field";

interface ClassModel {
    fullClassName: string;
    name: string
    fields: Field[]
}

export default ClassModel;