import { ElementValidatorBase } from '../element-validator-base';
import { CEGModel } from '../../model/CEGModel';
import { IContainer } from '../../model/IContainer';
import { ValidationResult } from '../validation-result';
import { Validator } from '../validator-decorator';
import { Type } from '../../util/type';
import { CEGNode } from '../../model/CEGNode';
import { Config } from '../../config/config';

@Validator(CEGModel)
export class EmptyModelValidator extends ElementValidatorBase<CEGModel> {
    public validate(element: any, contents: IContainer[]): ValidationResult {
        const valid: boolean = contents.some((element: IContainer) => Type.is(element, CEGNode));
        if (valid) {
            return ValidationResult.VALID;
        }
        return new ValidationResult(Config.ERROR_EMPTY_MODEL, false, []);
    }
}
