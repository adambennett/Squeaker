import { IUtilizer, NewUtilizer } from './utilizer.model';

export const sampleWithRequiredData: IUtilizer = {
  id: 54363,
  handle: 'Sports',
};

export const sampleWithPartialData: IUtilizer = {
  id: 57894,
  handle: 'AI Profound Run',
};

export const sampleWithFullData: IUtilizer = {
  id: 89736,
  handle: 'Robust Garden',
  followers: 22234,
  following: 38179,
};

export const sampleWithNewData: NewUtilizer = {
  handle: 'Small',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
