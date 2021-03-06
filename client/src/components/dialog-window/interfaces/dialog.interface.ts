import { CSSProperties, ReactNode } from 'react';
import { RouterProps } from 'react-router';
import { InputSettings } from './input.interface';

export interface DialogSettings {
  context?: any;
  header: string;
  button_text: string;
  validationSchema?: object;
  inputs: InputSettings[];
  inline_styles?: CSSProperties;
  handleSubmit: (data: any, categ?: string) => Promise<string>;
  redirect?: {
    routerProps: RouterProps
    redirectURL: string
  };
  childComponents?: ReactNode;
}
